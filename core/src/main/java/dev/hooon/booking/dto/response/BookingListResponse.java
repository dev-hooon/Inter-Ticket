package dev.hooon.booking.dto.response;

import java.util.List;

public record BookingListResponse(
	List<BookingResponse> bookingList
) {
}
