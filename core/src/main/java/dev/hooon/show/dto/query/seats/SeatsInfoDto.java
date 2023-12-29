package dev.hooon.show.dto.query.seats;

import dev.hooon.show.domain.entity.seat.SeatGrade;

public record SeatsInfoDto(
	SeatGrade grade,
	Long leftSeats,
	int price
) {
}
