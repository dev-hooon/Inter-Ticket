package dev.hooon.booking.application.fascade;

import static dev.hooon.booking.exception.BookingErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.booking.dto.BookingMapper;
import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.show.application.SeatService;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketBookingFacade {

	private final BookingService bookingService;
	private final SeatService seatService;
	private final UserService userService;

	private void validateAvailableSeat(Seat seat) {
		if (!seat.isBookingAvailable()) {
			throw new ValidationException(NOT_AVAILABLE_SEAT);
		}
	}

	private void validateSeatList(int seatIdsSize, List<Seat> seatList) {
		if (seatList.isEmpty()) {
			throw new ValidationException(INVALID_EMPTY_SEAT);
		}
		if (seatList.size() != seatIdsSize) {
			throw new ValidationException(INVALID_SELECTED_SEAT);
		}
	}

	@Transactional
	public TicketBookingResponse bookingTicket(
		Long userId,
		TicketBookingRequest ticketBookingRequest
	) {

		// 유저서비스에서 [유저] 가져옴
		User user = userService.getUserById(userId);

		// 좌석서비스에서 [모든 좌석들] 가져옴
		List<Seat> seatList = seatService.findByIdIn(ticketBookingRequest.seatIds());
		// 가져온 [모든 좌석들] validation
		validateSeatList(ticketBookingRequest.seatIds().size(), seatList);

		// 좌석에서 [공연] 가져옴
		Show show = seatList.get(0).getShow();

		// 모든 좌석들에 대해 [각 좌석] validation
		seatList.forEach(TicketBookingFacade::validateAvailableSeat);

		// 예매 생성
		Booking booking = Booking.of(user, show);

		// 예매에 티켓리스트 넣기
		seatList.forEach(seat -> {
			seat.markSeatStatusAsBooked();
			booking.addTicket(Ticket.of(seat));
		});

		// 예매 저장
		Booking savedbooking = bookingService.bookingTicket(booking);

		return BookingMapper.toTicketBookingResponse(savedbooking);
	}
}
