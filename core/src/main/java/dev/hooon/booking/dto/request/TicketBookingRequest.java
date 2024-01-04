package dev.hooon.booking.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record TicketBookingRequest(
	@NotNull
	List<Long> seatIds
) {
}
