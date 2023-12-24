package dev.hooon.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	private static final String ARGUMENT_NOT_VALID_MESSAGE = "잘못된 입력 값 입니다";
	private static final String ARGUMENT_NOT_VALID_ERROR_CODE = "G_001";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponseTemplate handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e
	) {
		log.error("MethodArgumentNotValidException : ", e);

		String message = ARGUMENT_NOT_VALID_MESSAGE;
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		if (!allErrors.isEmpty()) {
			message = allErrors.get(0).getDefaultMessage();
		}

		return new ErrorResponseTemplate(message, ARGUMENT_NOT_VALID_ERROR_CODE);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ErrorResponseTemplate handleNotFoundException(
		NotFoundException e
	) {
		log.error("NotFoundException : ", e);
		return new ErrorResponseTemplate(e.getErrorCode().getMessage(), e.getErrorCode().getCode());
	}

}
