package dev.hooon.booking.application.fascade;

import static dev.hooon.booking.exception.BookingErrorCode.*;
import static dev.hooon.user.domain.entity.UserRole.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import dev.hooon.booking.infrastructure.repository.BookingJpaRepository;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.TestContainerSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.infrastructure.repository.PlaceJpaRepository;
import dev.hooon.show.infrastructure.repository.SeatJpaRepository;
import dev.hooon.show.infrastructure.repository.ShowJpaRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.infrastructure.repository.UserJpaRepository;

@DisplayName("[TicketBookingFacade 통합 테스트]")
@SpringBootTest
class TicketBookingFacadeIntegrationTest extends TestContainerSupport {

	@Autowired
	private TicketBookingFacade ticketBookingFacade;

	@Autowired
	private SeatJpaRepository seatRepository;

	@Autowired
	private ShowJpaRepository showRepository;

	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private PlaceJpaRepository placeRepository;

	@Autowired
	private BookingJpaRepository bookingRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	void databaseCleanUp() {
		bookingRepository.deleteAll();
		placeRepository.deleteAll();
		userRepository.deleteAll();
		showRepository.deleteAll();
		seatRepository.deleteAll();

		redisTemplate.delete("*");
	}

	@DisplayName("좌석 예매 성공 시, 예매된 모든 좌석의 seatStatus 는 'BOOKED' 가 된다")
	@Test
	void bookingTicket_success_test() {

		// given
		// User user = new User("user@email.com", "user", BUYER);
		User user = User.ofBuyer("user@email.com", "user", BUYER.toString());
		userRepository.save(user);

		Place place = TestFixture.getPlace();
		placeRepository.save(place);

		Show show = TestFixture.getShow(place);
		showRepository.save(show);

		List<Seat> seats = TestFixture.getSeatList(show, show.getShowPeriod().getStartDate(), 1);
		List<Seat> savedSeats = seatRepository.saveAll(seats);
		List<Long> idList = savedSeats.stream()
			.map(Seat::getId)
			.toList();

		// when
		TicketBookingResponse ticketBookingResponse = ticketBookingFacade.bookingTicket(user.getId(), idList);

		// then
		assertThat(ticketBookingResponse.bookingTickets()).hasSize(5);
		ticketBookingResponse.bookingTickets().forEach(
			it -> assertEquals("BOOKED", it.seatStatus())
		);
	}

	@DisplayName("이미 예약이 되어있는 좌석이 포함된 경우 예매 전체가 취소된다")
	@Test
	void bookingTicket_fail_test_1() {

		// given
		// User user = new User("user@email.com", "user", BUYER);
		User user = User.ofBuyer("user@email.com", "user", BUYER.toString());
		userRepository.save(user);

		Place place = TestFixture.getPlace();
		placeRepository.save(place);

		Show show = TestFixture.getShow(place);
		showRepository.save(show);

		List<Seat> seats = TestFixture.getReservedSeats(show, show.getShowPeriod().getStartDate(), 1);
		List<Seat> savedSeats = seatRepository.saveAll(seats);
		List<Long> idList = savedSeats.stream()
			.map(Seat::getId)
			.toList();

		// when, then
		assertThrows(
			ValidationException.class,
			() -> ticketBookingFacade.bookingTicket(user.getId(), idList),
			NOT_AVAILABLE_SEAT.getMessage()
		);
	}

	@DisplayName("유효하지 않은 좌석이 포함된 경우 예매 전체가 취소된다")
	@Test
	void bookingTicket_fail_test_2() {

		// given
		// User user = new User("user@email.com", "user", BUYER);
		User user = User.ofBuyer("user@email.com", "user", BUYER.toString());
		userRepository.save(user);

		Place place = TestFixture.getPlace();
		placeRepository.save(place);

		Show show = TestFixture.getShow(place);
		showRepository.save(show);

		List<Seat> seats = TestFixture.getNonValidSeats(show, show.getShowPeriod().getStartDate(), 1);
		List<Seat> savedSeats = seatRepository.saveAll(seats);
		List<Long> idList = savedSeats.stream()
			.map(Seat::getId)
			.toList();

		// when, then
		assertThrows(
			ValidationException.class,
			() -> ticketBookingFacade.bookingTicket(user.getId(), idList),
			INVALID_SELECTED_SEAT.getMessage()
		);
	}

	@Test
	@DisplayName("[동시에 100개의 예매를 할 때, 단 한건의 예매만 성공한다]")
	void bookingTicket_concurrency_test() throws InterruptedException {
		//given
		User user = User.ofBuyer("user@email.com", "user", BUYER.toString());
		userRepository.save(user);

		Place place = TestFixture.getPlace();
		placeRepository.save(place);

		Show show = TestFixture.getShow(place);
		showRepository.save(show);

		List<Seat> seats = TestFixture.getSeatList(show, show.getShowPeriod().getStartDate(), 1);
		seatRepository.saveAll(seats);
		List<Long> seatIds = seats.stream().map(Seat::getId).toList();
		List<Long> seatIds1 = List.of(seatIds.get(0), seatIds.get(1), seatIds.get(2));
		List<Long> seatIds2 = List.of(seatIds.get(2), seatIds.get(3), seatIds.get(4));

		int threadCount = 100;
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

		//when
		for (int i = 0; i < 50; i++) {
			executorService.execute(() -> {
				try {
					ticketBookingFacade.bookingTicket(user.getId(), seatIds1);
				} catch (ValidationException e) {
					// 예외 catch 해서 정상흐름
				}

				countDownLatch.countDown();
			});
		}

		for (int i = 0; i < 50; i++) {
			executorService.execute(() -> {
				try {
					ticketBookingFacade.bookingTicket(user.getId(), seatIds2);
				} catch (ValidationException e) {
					// 예외 catch 해서 정상흐름
				}

				countDownLatch.countDown();
			});
		}

		countDownLatch.await();

		//then
		List<Booking> allBooking = bookingRepository.findAll();
		assertThat(allBooking).hasSize(1);
	}
}
