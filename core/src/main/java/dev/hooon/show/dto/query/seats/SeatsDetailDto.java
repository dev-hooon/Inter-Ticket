package dev.hooon.show.dto.query.seats;

import java.time.LocalDate;

import dev.hooon.show.domain.entity.seat.SeatStatus;

public record SeatsDetailDto(
	Long id,
	LocalDate date,
	boolean isSeat,
	String sector,
	String row,
	int col,
	int price,
	SeatStatus isBookingAvailable
) {
}
