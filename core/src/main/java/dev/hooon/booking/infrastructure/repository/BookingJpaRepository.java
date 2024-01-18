package dev.hooon.booking.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets WHERE b.id = :bookingId")
	Optional<Booking> findByIdWithTickets(@Param("bookingId") Long bookingId);

	@Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.createdAt >= :createdDateTime")
	Page<Booking> findBookingsByUserIdAndCreatedAtAfter(
		@Param("userId") Long userId,
		@Param("createdDateTime") LocalDateTime createdDateTime,
		Pageable pageable
	);

}
