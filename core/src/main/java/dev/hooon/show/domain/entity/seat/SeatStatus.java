package dev.hooon.show.domain.entity.seat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatStatus {

	AVAILABLE("예매 가능"),
	BOOKED("예매 완료"),
	CANCELED("예매 취소"),
	// 취소된 좌석이 예매대기로 인해 6시간동안 예매대기를 한 사용자에 한해 예약이 가능한 상태
	WAITING("예매 대기");

	private final String description;
}
