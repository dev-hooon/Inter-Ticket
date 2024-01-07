package dev.hooon.booking.infrastructure.adaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import dev.hooon.booking.domain.entity.Booking;
import dev.hooon.booking.domain.repository.BookingRepository;
import dev.hooon.booking.infrastructure.repository.BookingJpaRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryAdaptor implements BookingRepository {

	private final BookingJpaRepository bookingJpaRepository;

	@Override
	public Booking save(Booking booking) {
		return bookingJpaRepository.save(booking);
	}

	@Override
	public List<Booking> findAll() {
		return bookingJpaRepository.findAll();
	}

	@Override
	public Optional<Booking> findByIdWithTickets(Long id) {
		return bookingJpaRepository.findByIdWithTickets(id);
	}

	@Override
	public List<Booking> findByUserIdAndDays(
		Long userId,
		LocalDateTime createdDateTime,
		Pageable pageable
	) {
		Page<Booking> bookingPage = bookingJpaRepository.findBookingsByUserIdAndCreatedAtAfter(
			userId,
			createdDateTime,
			pageable
		);
		return bookingPage.getContent();
	}
}
