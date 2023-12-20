package dev.hooon.show.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AbleBookingDateRoundResponse(
	List<AvailableDate> availableDates
) {
	public record AvailableDate(
		LocalDate showDate,
		int round,
		LocalTime startTime
	) {
	}
}
