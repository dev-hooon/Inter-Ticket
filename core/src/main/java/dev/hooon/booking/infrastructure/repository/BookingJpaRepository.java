package dev.hooon.booking.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.booking.domain.entity.Booking;

public interface BookingJpaRepository extends JpaRepository<Booking, Long> {
}
