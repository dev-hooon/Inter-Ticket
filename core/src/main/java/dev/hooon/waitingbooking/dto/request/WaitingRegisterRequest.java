package dev.hooon.waitingbooking.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record WaitingRegisterRequest(
	@Positive
	int seatCount,
	@NotNull
	List<Long> seatIds
) {
}
