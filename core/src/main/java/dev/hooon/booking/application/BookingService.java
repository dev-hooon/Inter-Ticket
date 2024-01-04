package dev.hooon.booking.application;

import org.springframework.stereotype.Service;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.repository.BookingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;

	public Booking bookingTicket(Booking booking) {
		return bookingRepository.save(booking);
	}
}
