package dev.hooon.booking.dto.response;

import java.util.List;

public record BookingCancelResponse(
	String bookingStatus,
	List<TicketSeatResponse> canceledTickets
) {
}
