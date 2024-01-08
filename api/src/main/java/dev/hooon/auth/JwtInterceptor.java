package dev.hooon.auth;

import static dev.hooon.auth.exception.AuthErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	private final JwtProvider jwtProvider;

	private boolean isAnnotationPresent(Object handler) {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		return handlerMethod.getMethodAnnotation(NoAuth.class) != null;
	}

	@Override
	public boolean preHandle(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler
	) {
		if (isAnnotationPresent(handler)) {
			return true;
		}

		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (accessToken == null) {
			throw new AuthException(NOT_INCLUDE_ACCESS_TOKEN);
		}

		jwtProvider.validateToken(accessToken);
		return true;
	}
}
