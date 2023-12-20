package dev.hooon.show.domain.entity.seat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatStatus {

	AVAILABLE_BOOKING("예매 가능"),
	BOOKED("예매 완료"),
	CANCELED("예매 취소");

	private final String description;
}
