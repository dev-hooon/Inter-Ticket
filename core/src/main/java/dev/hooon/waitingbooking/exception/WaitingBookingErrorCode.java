package dev.hooon.waitingbooking.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WaitingBookingErrorCode implements ErrorCode {

	INVALID_SEAT_COUNT("좌석 개수는 1~3 개 내로 선택해야합니다", "W_001"),
	EMPTY_SELECTED_SEAT("좌석은 반드시 1개 이상 선택해야합니다", "W_002"),
	INVALID_SELECTED_SEAT_COUNT("좌석은 선택한 좌석 개수에서 10배수 까지 선택 가능합니다", "W_003"),
	INVALID_CONFIRMED_SEAT_COUNT("확정 좌석 개수는 좌석개수와 동일해야합니다", "W_004"),
	NOT_FOUND_BY_ID("id 에 해당하는 WaitingBooking 이 존재하지 않습니다", "W_005");

	private final String message;
	private final String code;
}
