package dev.hooon.show.dto.query;

import java.time.LocalDate;
import java.time.LocalTime;

public record SeatDateRoundDto(
	LocalDate showDate,
	int round,
	LocalTime startTime
) {
}
