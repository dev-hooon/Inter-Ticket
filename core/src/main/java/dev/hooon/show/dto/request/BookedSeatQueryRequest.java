package dev.hooon.show.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookedSeatQueryRequest(
	@NotNull
	Long showId,
	@NotNull
	LocalDate date,
	@Positive
	int round
) {
}
