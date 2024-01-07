package dev.hooon.booking.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.common.fixture.SeatFixture;
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
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
class BookingJpaRepositoryTest extends TestContainerSupport {

	@Autowired
	private BookingJpaRepository bookingRepository;

	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private ShowJpaRepository showRepository;

	@Autowired
	private SeatJpaRepository seatRepository;

	@Autowired
	private PlaceJpaRepository placeRepository;

	@DisplayName("예약 조회 시 예약 티켓까지 한 번에 fetch join으로 데이터를 가져온다.")
	@Test
	@Transactional
	void findByIdWithTickets_test() {
		// given
		User user = TestFixture.getUser(1L);
		userRepository.save(user);
		Place place = TestFixture.getPlace();
		placeRepository.save(place);
		Show show = TestFixture.getShow(place);
		showRepository.save(show);
		Seat seat1 = SeatFixture.getSeat();
		Seat seat2 = SeatFixture.getSeat();
		seatRepository.saveAll(List.of(seat1, seat2));
		Booking booking = Booking.of(user, show);
		booking.addTicket(Ticket.of(seat1));
		booking.addTicket(Ticket.of(seat2));
		Booking savedBooking = bookingRepository.save(booking);

		// when
		Optional<Booking> bookingWithTickets = bookingRepository.findByIdWithTickets(savedBooking.getId());

		// then
		assertAll(
			() -> assertThat(bookingWithTickets).isPresent(),
			() -> assertThat(bookingWithTickets.get().getTickets()).hasSize(2)
		);
		List<Ticket> tickets = bookingWithTickets.get().getTickets();
		List<Seat> seats = tickets.stream().map(Ticket::getSeat).toList();
		assertThat(seats).containsExactlyInAnyOrderElementsOf(List.of(seat1, seat2));
	}

	@DisplayName("createdAt 이후의 예약만을 조회할 수 있다.")
	@Test
	void findBookingsByUserIdAndCreatedAtAfter_test() {

		// given
		User user = TestFixture.getUser(1L);
		userRepository.save(user);
		Place place = TestFixture.getPlace();
		placeRepository.save(place);

		Show show1 = TestFixture.getShow(place);
		showRepository.save(show1);
		Show show2 = TestFixture.getShow(place);
		showRepository.save(show2);

		Booking booking1 = Booking.of(user, show1, LocalDateTime.of(2023, 1, 1, 0, 0));
		Booking booking2 = Booking.of(user, show2, LocalDateTime.of(2024, 1, 1, 0, 0));
		Booking savedBooking1 = bookingRepository.save(booking1);
		Booking savedBooking2 = bookingRepository.save(booking2);

		// when
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<Booking> resultPage1 = bookingRepository.findBookingsByUserIdAndCreatedAtAfter(
			user.getId(), savedBooking2.getCreatedAt(), pageRequest);
		Page<Booking> resultPage2 = bookingRepository.findBookingsByUserIdAndCreatedAtAfter(
			user.getId(), savedBooking1.getCreatedAt(), pageRequest);

		// then
		assertAll(
			() -> assertThat(resultPage1.getContent()).hasSize(1),
			() -> assertThat(resultPage2.getContent()).hasSize(2)
		);
	}
}
