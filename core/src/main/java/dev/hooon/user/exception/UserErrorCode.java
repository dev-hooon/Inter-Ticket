package dev.hooon.user.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	NOT_FOUND_BY_ID("ID 에 해당하는 사용자가 존재하지 않습니다", "U_001"),
	NOT_FOUND_BY_EMAIL("EMAIL 에 해당하는 사용자가 존재하지 않습니다", "U_002");

	private final String message;
	private final String code;
}
