package dev.hooon.booking.application.fascade;

import static dev.hooon.booking.exception.BookingErrorCode.*;
import static dev.hooon.user.domain.entity.UserRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.show.application.SeatService;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;

@DisplayName("[TicketBookingFacade 메서드 단위 테스트]")
@ExtendWith(MockitoExtension.class)
class TicketBookingFacadeTest {

	@InjectMocks
	private TicketBookingFacade ticketBookingFacade;

	@Mock
	private BookingService bookingService;

	@Mock
	private SeatService seatService;

	@Mock
	private UserService userService;

	@DisplayName("seatList가 비어있으면 예외가 발생한다")
	@Test
	void validateSeatList_test_1() {

		// given
		User user = new User("user@email.com", "user", BUYER);
		TicketBookingRequest ticketBookingRequest = new TicketBookingRequest(List.of());

		when(userService.getUserById(1L)).thenReturn(user);

		// when, then
		assertThrows(
			ValidationException.class,
			() -> ticketBookingFacade.bookingTicket(1L, ticketBookingRequest),
			INVALID_EMPTY_SEAT.getMessage()
		);
	}

	@DisplayName("요청한 좌석수와 seatList의 사이즈가 다르면 예외가 발생한다")
	@Test
	void validateSeatList_test_2() {

		// given
		User user = new User("user@email.com", "user", BUYER);
		TicketBookingRequest ticketBookingRequest = new TicketBookingRequest(List.of(1L, 2L, 3L));

		when(userService.getUserById(1L)).thenReturn(user);
		when(seatService.findByIdIn(ticketBookingRequest.seatIds())).thenReturn(List.of());

		// when, then
		assertThrows(
			ValidationException.class,
			() -> ticketBookingFacade.bookingTicket(1L, ticketBookingRequest),
			INVALID_SELECTED_SEAT.getMessage()
		);
	}

	@DisplayName("좌석들 중 seatStatus가 AVAILABLE 이지 않은 좌석이 하나라도 있으면 예외가 발생한다.")
	@Test
	void validateAvailableSeat_test() {

		// given
		User user = new User("user@email.com", "user", BUYER);
		Place place = TestFixture.getPlace();
		Show show = TestFixture.getShow(place);
		List<Seat> seatList = TestFixture.getSeatList(show, show.getShowPeriod().getStartDate(), 1);
		TicketBookingRequest ticketBookingRequest = new TicketBookingRequest(
			seatList.stream().map(Seat::getId).toList());
		seatList.get(0).markSeatStatusAsBooked();

		when(userService.getUserById(1L)).thenReturn(user);
		when(seatService.findByIdIn(seatList.stream().map(Seat::getId).toList())).thenReturn(seatList);

		// when, then
		assertThrows(
			ValidationException.class,
			() -> ticketBookingFacade.bookingTicket(1L, ticketBookingRequest),
			NOT_AVAILABLE_SEAT.getMessage()
		);
	}

}
