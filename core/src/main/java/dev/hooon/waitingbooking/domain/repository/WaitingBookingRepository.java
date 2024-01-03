package dev.hooon.waitingbooking.domain.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;

public interface WaitingBookingRepository {

	void save(WaitingBooking waitingBooking);

	Optional<WaitingBooking> findById(Long id);

	List<WaitingBooking> findAll();

	// WaitingStatus 가 WAITING 인 데이터를 최신순으로 조회하는 쿼리(fetch join selectedSeat + user)
	List<WaitingBooking> findWithSelectedSeatsByStatus(WaitingStatus status);

	// WaitingStatus 가 WAITING 인 데이터를 최신순으로 조회하는 쿼리 (fetch join confirmedSeat)
	List<WaitingBooking> findWithConfirmedSeatsByStatus(WaitingStatus status);

	void updateToActiveById(Long id);

	void updateStatusByIdIn(WaitingStatus status, Collection<Long> targetIds);
}
