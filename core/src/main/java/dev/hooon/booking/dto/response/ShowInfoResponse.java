package dev.hooon.booking.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ShowInfoResponse(
	String showName,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate showDate,
	int showRound,
	@JsonFormat(pattern = "HH:mm:ss")
	LocalTime showRoundStartTime
) {
}
