package dev.hooon.common.exception;

public record ErrorResponseTemplate(
	String message,
	String code
) {
}
