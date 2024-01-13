package dev.hooon.booking.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record BookingResponse(
	long bookingId,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate bookingDate,
	ShowInfoResponse showInfo,
	int ticketNumber,
	String currentState
) {
}
