package dev.hooon.waitingbooking.domain.repository;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;

public interface WaitingBookingRepository {

	void save(WaitingBooking waitingBooking);
}
