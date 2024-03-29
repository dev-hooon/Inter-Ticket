package dev.hooon.booking.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.common.support.DataJpaTestSupport;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.infrastructure.repository.PlaceJpaRepository;
import dev.hooon.show.infrastructure.repository.SeatJpaRepository;
import dev.hooon.show.infrastructure.repository.ShowJpaRepository;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.infrastructure.repository.UserJpaRepository;
import jakarta.transaction.Transactional;

class BookingJpaRepositoryTest extends DataJpaTestSupport {

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
		Booking booking = Booking.of(user, show, List.of(seat1, seat2));
		Booking savedBooking = bookingRepository.save(booking);

		// when
		Optional<Booking> bookingWithTickets = bookingRepository.findByIdWithTickets(savedBooking.getId());

		// then
		assertThat(bookingWithTickets).isPresent();
		assertThat(bookingWithTickets.get().getTickets()).hasSize(2);

		List<Ticket> tickets = bookingWithTickets.get().getTickets();
		List<Seat> seats = tickets.stream().map(Ticket::getSeat).toList();
		assertThat(seats).containsExactlyInAnyOrderElementsOf(List.of(seat1, seat2));
	}
}
