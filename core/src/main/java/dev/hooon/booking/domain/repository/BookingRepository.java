package dev.hooon.booking.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingRepository {

	Booking save(Booking booking);

	List<Booking> findAll();

	Optional<Booking> findByIdWithTickets(Long id);
}
