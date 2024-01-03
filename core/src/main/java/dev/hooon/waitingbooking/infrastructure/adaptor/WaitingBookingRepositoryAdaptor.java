package dev.hooon.waitingbooking.infrastructure.adaptor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;
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

	@Override
	public Optional<WaitingBooking> findById(Long id) {
		return waitingBookingJpaRepository.findById(id);
	}

	@Override
	public List<WaitingBooking> findAll() {
		return waitingBookingJpaRepository.findAll();
	}

	@Override
	public List<WaitingBooking> findWithSelectedSeatsByStatus(WaitingStatus status) {
		return waitingBookingJpaRepository.findWithSelectedSeatsByStatusOrderByIdDesc(status);
	}

	@Override
	public List<WaitingBooking> findWithConfirmedSeatsByStatus(WaitingStatus status) {
		return waitingBookingJpaRepository.findWithConfirmedSeatsByStatusOrderByIdDesc(status);
	}

	@Override
	public void updateToActiveById(Long id) {
		waitingBookingJpaRepository.updateStatusAndExpireAt(
			id,
			WaitingStatus.ACTIVATION,
			LocalDateTime.now().plusHours(6)
		);
	}

	@Override
	public void updateStatusByIdIn(WaitingStatus status, Collection<Long> targetIds) {
		waitingBookingJpaRepository.updateStatusByIdIn(status, targetIds);
	}
}
