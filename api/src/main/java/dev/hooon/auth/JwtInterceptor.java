package dev.hooon.auth;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static dev.hooon.auth.exception.AuthErrorCode.*;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

	private final JwtProvider jwtProvider;

	private boolean checkAnnotationIsNotPresent(Object handler, Class cls) {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		if (handlerMethod.getMethodAnnotation(cls) == null) { //해당 어노테이션이 존재하지 않으면 true
			return true;
		}
		return false;
	}

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) {
		boolean check = checkAnnotationIsNotPresent(handler, NeedAuth.class);
		if (check) {
			return true;
		}

		String accessToken = request.getHeader(ACCESS.getHeaderKey());
		String refreshToken = request.getHeader(REFRESH.getHeaderKey());

		if (accessToken != null) {
			if (jwtProvider.validateToken(accessToken, ACCESS)) {
				return true;
			} else {
				throw new AuthException(INVALID_ACCESS_TOKEN);
			}
		} else {
			if (refreshToken != null) {    // 액세스 토큰이 만료되어서 재발급해야 할 때
				if (jwtProvider.validateToken(refreshToken, REFRESH)) {
					return true;
				} else {
					throw new AuthException(INVALID_REFRESH_TOKEN);
				}
			}
			throw new AuthException(NOT_INCLUDE_ACCESS_TOKEN);
		}
	}
}
