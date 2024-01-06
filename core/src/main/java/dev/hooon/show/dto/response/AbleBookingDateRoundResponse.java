package dev.hooon.show.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AbleBookingDateRoundResponse(
	List<AvailableDate> availableDates
) {
	public record AvailableDate(
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate showDate,
		int round,
		@JsonFormat(pattern = "HH:mm:ss")
		LocalTime startTime
	) {
	}
}
