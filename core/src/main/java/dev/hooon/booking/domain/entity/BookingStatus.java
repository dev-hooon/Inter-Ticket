package dev.hooon.booking.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {

	BOOKED("예매 완료"),
	CANCELED("예매 취소");

	private final String description;
}
