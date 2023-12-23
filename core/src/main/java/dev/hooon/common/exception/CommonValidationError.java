package dev.hooon.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonValidationError {

	private static final String NOT_NULL_POSTFIX = " 는 Null 이 될 수 없습니다";
	private static final String NOT_EMPTY_POSTFIX = " 는 Empty 가 될 수 없습니다";

	public static String getNotNullMessage(String object, String variable) {
		return object + "_" + variable + NOT_NULL_POSTFIX;
	}

	public static String getNotEmptyPostfix(String object, String variable) {
		return object + "_" + variable + NOT_EMPTY_POSTFIX;
	}
}
