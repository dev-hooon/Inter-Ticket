package dev.hooon.waitingbooking.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.entity.WaitingStatus;

public interface WaitingBookingJpaRepository extends JpaRepository<WaitingBooking, Long> {

	@EntityGraph(attributePaths = {"user", "selectedSeats"})
	List<WaitingBooking> findWithSelectedSeatsByStatusOrderByIdDesc(WaitingStatus status);

	@EntityGraph(attributePaths = {"confirmedSeats"})
	List<WaitingBooking> findWithConfirmedSeatsByStatusOrderByIdDesc(WaitingStatus status);

	@Modifying
	@Query("update WaitingBooking w SET w.status = :status, w.expiredAt = :expireAt where w.id = :id")
	void updateStatusAndExpireAt(
		@Param("id") Long id,
		@Param("status") WaitingStatus status,
		@Param("expireAt") LocalDateTime expireAt);

	@Modifying
	@Query("update WaitingBooking w SET w.status = :status where w.id in :ids")
	void updateStatusByIdIn(@Param("status") WaitingStatus status, @Param("ids") Collection<Long> ids);
}
