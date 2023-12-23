package dev.hooon.waitingbooking.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WaitingStatus {

	/**
	 * 	   			예약 가능 여부 | 시간 만료
	 * WAITING     			 X | X
	 * ACTIVATION  			 O | O
	 * EXPIRED				 X | O
	 * FIN					 X | X
	 */
	WAITING("대기"),
	ACTIVATION("활성"),
	EXPIRED("만료"),
	FIN("종료");

	private final String description;
}
