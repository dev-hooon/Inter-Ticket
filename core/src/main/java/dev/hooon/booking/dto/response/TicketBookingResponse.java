package dev.hooon.booking.dto.response;

import java.util.List;

public record TicketBookingResponse(
	long bookingId,
	List<TicketResponse> bookingTickets
) {
}
