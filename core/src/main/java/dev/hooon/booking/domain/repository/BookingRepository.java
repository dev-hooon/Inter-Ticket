package dev.hooon.booking.domain.repository;

import java.util.List;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingRepository {

	Booking save(Booking booking);

	List<Booking> findAll();
}
