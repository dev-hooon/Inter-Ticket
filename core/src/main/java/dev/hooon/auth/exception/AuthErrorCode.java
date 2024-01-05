package dev.hooon.auth.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	FAILED_LOGIN_BY_ANYTHING("등록되지 않은 아이디이거나 아이디 또는 비밀번호를 잘못 입력했습니다.", "A_001"),
	NOT_PERMITTED_USER("해당 요청에 대한 권한이 없습니다.", "A_002"),
	INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다.", "A_003"),
	INVALID_REFRESH_TOKEN("유효하지 않은 리프레쉬 토큰입니다.", "A_004"),
	NOT_FOUND_REFRESH_TOKEN("해당 리프레쉬 토큰의 인증 데이터가 존재하지 않습니다.", "A_005"),
	NOT_INCLUDE_ACCESS_TOKEN("요청에 액세스 토큰이 존재하지 않습니다.", "A_006"),
	NOT_FOUND_REQUEST("해당 요청이 존재하지 않습니다.", "A_007"),
	NOT_FOUND_USER_ID("해당 유저의 인증 데이터가 존재하지 않습니다.", "A_008");

	private final String message;
	private final String code;
}
