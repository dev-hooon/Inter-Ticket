package dev.hooon.waitingbooking.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hooon.waitingbooking.domain.entity.WaitingBooking;

public interface WaitingBookingJpaRepository extends JpaRepository<WaitingBooking, Long> {
}
