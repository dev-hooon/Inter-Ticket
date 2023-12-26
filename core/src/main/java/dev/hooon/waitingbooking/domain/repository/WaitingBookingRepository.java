package dev.hooon.waitingbooking.domain.repository;

import java.util.List;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;

public interface WaitingBookingRepository {

	void save(WaitingBooking waitingBooking);

	List<WaitingBooking> findAll();
}
