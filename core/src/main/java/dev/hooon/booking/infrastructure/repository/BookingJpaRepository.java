package dev.hooon.booking.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT DISTINCT b FROM Booking b LEFT JOIN FETCH b.tickets WHERE b.id = :bookingId")
	Optional<Booking> findByIdWithTickets(@Param("bookingId") Long bookingId);
}
