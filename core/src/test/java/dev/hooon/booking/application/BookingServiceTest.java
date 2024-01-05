package dev.hooon.booking.application;

import static dev.hooon.booking.exception.BookingErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.BookingStatus;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.booking.dto.response.BookingCancelResponse;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.entity.seat.SeatStatus;
import dev.hooon.user.domain.entity.User;

@DisplayName("[BookingService 테스트]")
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@InjectMocks
	private BookingService bookingService;

	@Mock
	private BookingRepository bookingRepository;

	@DisplayName("취소하려는 예약이 존재하지 않으면 예외가 발생한다.")
	@Test
	void cancelBooking_fail_test_1() {
		// given
		long userId = 1L;
		long bookingId = 1L;
		given(bookingRepository.findById(1L)).willReturn(Optional.empty());

		// when, then
		assertThrows(
			NotFoundException.class,
			() -> bookingService.cancelBooking(userId, bookingId),
			BOOKING_NOT_FOUND.getMessage()
		);
	}

	@DisplayName("예약 취소하려는 유저와 예약을 한 유저가 다르면 예외가 발생한다.")
	@Test
	void cancelBooking_fail_test_2() {
		// given
		long bookingId = 1L;
		long anotherUserId = 2L;
		long userId = 1L;
		User user = TestFixture.getUser(userId);
		Booking booking = Booking.of(user, TestFixture.getShow(TestFixture.getPlace()));

		given(bookingRepository.findById(bookingId)).willReturn(Optional.of(booking));

		// when, then
		assertThrows(
			ValidationException.class,
			() -> bookingService.cancelBooking(anotherUserId, bookingId),
			NOT_IDENTICAL_USER.getMessage()
		);
	}

	@DisplayName("이미 예약이 취소된 예약을 다시 취소하려고 하면 예외가 발생한다.")
	@Test
	void cancelBooking_fail_test_3() {
		// given
		long bookingId = 1L;
		long userId = 1L;
		User user = TestFixture.getUser(userId);
		Booking booking = Booking.of(user, TestFixture.getShow(TestFixture.getPlace()));
		booking.markBookingStatusAsCanceled();

		given(bookingRepository.findById(bookingId)).willReturn(Optional.of(booking));

		// when, then
		assertThrows(
			ValidationException.class,
			() -> bookingService.cancelBooking(userId, bookingId),
			ALREADY_CANCELED_BOOKING.getMessage()
		);
	}

	@DisplayName("예약 취소 시, 예약 상태 그리고 예약의 모든 좌석 상태가 '취소' 상태로 변경된다.")
	@Test
	void cancelBooking_success_test() {
		// given
		long bookingId = 1L;
		long userId = 1L;
		User user = TestFixture.getUser(userId);
		Place place = TestFixture.getPlace();
		Show show = TestFixture.getShow(place);
		List<Seat> allBookedSeats = TestFixture.getAllBookedSeats(
			show,
			show.getShowPeriod().getStartDate(),
			1
		);
		Booking booking = Booking.of(user, show);
		allBookedSeats.forEach(
			seat -> booking.addTicket(Ticket.of(seat))
		);
		given(bookingRepository.findById(bookingId)).willReturn(Optional.of(booking));

		// when
		BookingCancelResponse bookingCancelResponse = bookingService.cancelBooking(userId, bookingId);

		// then
		assertEquals(BookingStatus.CANCELED.toString(), bookingCancelResponse.bookingStatus());
		bookingCancelResponse.canceledTickets()
			.forEach(ticket -> assertEquals(SeatStatus.CANCELED.toString(), ticket.seatStatus()));
	}
}
