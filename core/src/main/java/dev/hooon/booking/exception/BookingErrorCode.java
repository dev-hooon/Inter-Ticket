package dev.hooon.booking.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode implements ErrorCode {

	INVALID_EMPTY_SEAT("좌석이 선택되지 않았습니다", "B_001"),
	INVALID_SELECTED_SEAT("선택한 좌석값들 중 유효하지 않은 좌석값이 있습니다", "B_002"),
	NOT_AVAILABLE_SEAT("좌석 중 예매가 불가능한 좌석이 있습니다", "B_003"),
	BOOKING_NOT_FOUND("예매를 찾을 수 없습니다", "B_004"),
	NOT_IDENTICAL_USER("예매한 유저가 동일하지 않습니다", "B_005"),
	ALREADY_CANCELED_BOOKING("이미 취소된 예약입니다", "B_006");

	private final String message;
	private final String code;
}
