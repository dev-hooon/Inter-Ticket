package dev.hooon.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

	private final String code;

	public NotFoundException(ErrorCode errorCode) {
		this.code = errorCode.getCode();
	}
}
