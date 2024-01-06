package dev.hooon.booking.application;

import static dev.hooon.booking.exception.BookingErrorCode.*;

import org.springframework.stereotype.Service;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.entity.BookingStatus;
import dev.hooon.booking.domain.entity.Ticket;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.booking.dto.BookingMapper;
import dev.hooon.booking.dto.response.BookingCancelResponse;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.common.exception.ValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;

	private void validateIdenticalUser(Long userId, Booking booking) {
		if (booking.getUserId() != userId) {
			throw new ValidationException(NOT_IDENTICAL_USER);
		}
	}

	private void validateBookingStatus(Booking booking) {
		if (booking.getBookingStatus() != BookingStatus.BOOKED) {
			throw new ValidationException(ALREADY_CANCELED_BOOKING);
		}
	}

	@Transactional
	public BookingCancelResponse cancelBooking(
		Long userId,
		Long bookingId
	) {
		// 1. bookingId로 booking 가져오기
		// 1.1 해당 userId와 동일한지 확인
		// 1.2 bookingStatus 확인
		Booking booking = getById(bookingId);
		validateIdenticalUser(userId, booking);
		validateBookingStatus(booking);

		// 2. booking의 모든 티켓들의 좌석 상태 'SeatStatus' 변경시키기 : BOOKED -> CANCELED
		booking.getTickets()
			.forEach(Ticket::markSeatStatusAsCanceled);

		// 3. booking의 'BookingStatus' 변경시키기 : BOOKED -> CANCELED
		booking.markBookingStatusAsCanceled();

		return BookingMapper.toBookingCancelResponse(booking);
	}

	public Booking bookingTicket(Booking booking) {
		return bookingRepository.save(booking);
	}

	public Booking getById(Long id) {
		return bookingRepository.findByIdWithTickets(id).orElseThrow(
			() -> new NotFoundException(BOOKING_NOT_FOUND)
		);
	}

}
