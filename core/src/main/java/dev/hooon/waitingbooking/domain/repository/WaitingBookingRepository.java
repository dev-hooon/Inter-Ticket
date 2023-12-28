package dev.hooon.waitingbooking.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;

public interface WaitingBookingRepository {

	void save(WaitingBooking waitingBooking);

	Optional<WaitingBooking> findById(Long id);

	List<WaitingBooking> findAll();

	// WaitingStatus 가 WAITING 인 데이터를 최신순으로 조회하는 쿼리
	List<WaitingBooking> findByStatusIsWaiting();

	void updateToActiveById(Long id);
}
