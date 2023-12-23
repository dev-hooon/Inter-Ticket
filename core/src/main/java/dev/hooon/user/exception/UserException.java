package dev.hooon.user.exception;

import dev.hooon.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

	private final String code;

	public UserException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}
}
