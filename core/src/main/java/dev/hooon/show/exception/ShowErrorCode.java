package dev.hooon.show.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShowErrorCode implements ErrorCode {

	SHOW_NOT_FOUND("공연을 찾을 수 없습니다.", "S_001"),
	SHOW_NAME_NOT_FOUND("좌석에 해당하는 공연의 이름을 찾을 수 없습니다", "S_002"),
	SHOW_CATEGORY_NOT_FOUND("지원하지 않는 공연 카테고리입니다", "S_003"),
	SHOW_PERIOD_TYPE_NOT_FOUND("지원하지 않는 랭킹 집계 기간입니다", "S_004");

	private final String message;
	private final String code;
}
