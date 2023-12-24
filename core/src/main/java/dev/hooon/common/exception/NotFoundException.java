package dev.hooon.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

	private final ErrorCode errorCode;

	public NotFoundException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
