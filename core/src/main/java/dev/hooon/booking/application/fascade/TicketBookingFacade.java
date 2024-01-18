package dev.hooon.booking.application.fascade;

import static dev.hooon.booking.exception.BookingErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.hooon.booking.aop.BookingConcurrency;
import dev.hooon.booking.application.BookingService;
import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.dto.BookingMapper;
import dev.hooon.booking.dto.response.TicketBookingResponse;
import dev.hooon.common.exception.ValidationException;
import dev.hooon.show.application.SeatService;
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

	private void validateSeatsAbleToBook(List<Seat> seats) {
		seats.forEach(seat -> {
			if (!seat.isBookingAvailable()) {
				throw new ValidationException(NOT_AVAILABLE_SEAT);
			}
		});
	}

	private void validateSeatsPresent(int seatIdsSize, List<Seat> seatList) {
		if (seatList.isEmpty()) {
			throw new ValidationException(INVALID_EMPTY_SEAT);
		}
		if (seatList.size() != seatIdsSize) {
			throw new ValidationException(INVALID_SELECTED_SEAT);
		}
	}

	@Transactional
	@BookingConcurrency
	public TicketBookingResponse bookingTicket(Long userId, List<Long> seatIds) {
		User user = userService.getUserById(userId);
		List<Seat> seats = seatService.findByIdIn(seatIds);

		// 좌석 검증
		validateSeatsPresent(seatIds.size(), seats);
		validateSeatsAbleToBook(seats);

		// 예매 생성 후 저장 & 좌석 상태 '예매됨' 상태로 변경
		Booking booking = Booking.of(user, seats.get(0).getShow(), seats);
		bookingService.bookingTicket(booking);

		return BookingMapper.toTicketBookingResponse(booking);
	}
}
