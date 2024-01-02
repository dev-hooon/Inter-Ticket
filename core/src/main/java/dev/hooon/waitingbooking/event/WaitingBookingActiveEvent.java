package dev.hooon.waitingbooking.event;

public record WaitingBookingActiveEvent(
	String nickname,
	String email,
	Long seatId
) {
}
