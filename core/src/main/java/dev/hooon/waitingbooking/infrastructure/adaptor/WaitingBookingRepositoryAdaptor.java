package dev.hooon.waitingbooking.infrastructure.adaptor;

import org.springframework.stereotype.Repository;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.infrastructure.repository.WaitingBookingJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WaitingBookingRepositoryAdaptor implements WaitingBookingRepository {

	private final WaitingBookingJpaRepository waitingBookingJpaRepository;

	@Override
	public void save(WaitingBooking waitingBooking) {
		waitingBookingJpaRepository.save(waitingBooking);
	}
}
