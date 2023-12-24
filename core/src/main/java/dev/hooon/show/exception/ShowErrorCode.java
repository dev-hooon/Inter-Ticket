package dev.hooon.show.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShowErrorCode implements ErrorCode {

	SHOW_NOT_FOUND("공연을 찾을 수 없습니다.", "S_001");

	private final String message;
	private final String code;
}
