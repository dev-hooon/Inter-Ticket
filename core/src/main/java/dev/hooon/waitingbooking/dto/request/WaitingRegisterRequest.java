package dev.hooon.waitingbooking.dto.request;

import java.util.List;

public record WaitingRegisterRequest(
	int seatCount,
	List<Long> seatIds
) {
}
