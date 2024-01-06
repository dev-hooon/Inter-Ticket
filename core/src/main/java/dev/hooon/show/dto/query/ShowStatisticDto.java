package dev.hooon.show.dto.query;

import java.time.LocalDate;

public record ShowStatisticDto(
	String showName,
	String placeName,
	LocalDate showStartDate,
	LocalDate showEndDate,
	long totalTicketCount
) {
}
