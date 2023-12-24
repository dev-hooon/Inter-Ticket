package dev.hooon.common.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

	private final String code;

	public ValidationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}
}
