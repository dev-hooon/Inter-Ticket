package dev.hooon.booking.application.fascade;

import static dev.hooon.booking.exception.BookingErrorCode.*;
import static dev.hooon.user.domain.entity.UserRole.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.dto.request.TicketBookingRequest;
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
@AutoConfigureTestDatabase(replace = NONE)
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

	@AfterEach
	void databaseCleanUp() {
		bookingRepository.deleteAll();
		placeRepository.deleteAll();
		userRepository.deleteAll();
		showRepository.deleteAll();
		seatRepository.deleteAll();
	}

	@DisplayName("좌석 예매 성공 시, 예매된 모든 좌석의 seatStatus는 'BOOKED' 가 된다")
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
		TicketBookingResponse ticketBookingResponse = ticketBookingFacade.bookingTicket(user.getId(),
			new TicketBookingRequest(idList));

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
			() -> ticketBookingFacade.bookingTicket(user.getId(), new TicketBookingRequest(idList)),
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
			() -> ticketBookingFacade.bookingTicket(user.getId(), new TicketBookingRequest(idList)),
			INVALID_SELECTED_SEAT.getMessage()
		);
	}

	@DisplayName("동시에 같은 좌석들을 예매할 때 예매는 1건만 만들어진다")
	@Test
	void bookingTicket_concurrency_test_1() throws InterruptedException {

		// given
		ExecutorService executorService = Executors.newFixedThreadPool(100);

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
		executorService.invokeAll(getCallables1(user, idList));

		// then
		List<Booking> bookingList = bookingRepository.findAll();
		assertEquals(1, bookingList.size());
	}

	@DisplayName("동시에 여러 예매가 진행될 때, 겹치는 좌석이 있으면 예매는 그 중 한 건만 만들어진다")
	@Test
	void bookingTicket_concurrency_test_2() throws InterruptedException {

		// given
		ExecutorService executorService = Executors.newFixedThreadPool(3);

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
		executorService.invokeAll(getCallables2(user, idList));

		// then
		List<Booking> bookingList = bookingRepository.findAll();
		assertEquals(2, bookingList.size());
	}

	private List<Callable<TicketBookingResponse>> getCallables1(User user, List<Long> idList) {
		List<Callable<TicketBookingResponse>> callables = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			callables.add(() -> ticketBookingFacade.bookingTicket(user.getId(), new TicketBookingRequest(idList)));
		}
		return callables;
	}

	private List<Callable<TicketBookingResponse>> getCallables2(User user, List<Long> idList) {
		List<Callable<TicketBookingResponse>> callables = new ArrayList<>();
		callables.add(
			() -> ticketBookingFacade.bookingTicket(user.getId(),
				new TicketBookingRequest(List.of(idList.get(0), idList.get(1), idList.get(2)))));
		callables.add(
			() -> ticketBookingFacade.bookingTicket(user.getId(),
				new TicketBookingRequest(List.of(idList.get(2), idList.get(3)))));
		callables.add(
			() -> ticketBookingFacade.bookingTicket(user.getId(), new TicketBookingRequest(List.of(idList.get(4)))));
		return callables;
	}

}
