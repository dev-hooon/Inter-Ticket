package dev.hooon.common.exception;

public record ErrorResponse(
	String message,
	String code
) {
}
