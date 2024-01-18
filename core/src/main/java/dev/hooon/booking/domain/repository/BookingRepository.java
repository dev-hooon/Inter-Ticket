package dev.hooon.booking.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingRepository {

	Booking save(Booking booking);

	List<Booking> findAll();

	Optional<Booking> findByIdWithTickets(Long id);

	List<Booking> findByUserIdAndDays(
		Long userId,
		LocalDateTime createdDateTime,
		Pageable pageable
	);
}
