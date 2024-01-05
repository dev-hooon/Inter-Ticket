package dev.hooon.auth;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static dev.hooon.auth.exception.AuthErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.auth.domain.entity.UserInfo;
import dev.hooon.auth.exception.AuthException;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JwtAuthorization.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		if (httpServletRequest != null) {
			String accessToken = httpServletRequest.getHeader(ACCESS.getHeaderKey());

			if (accessToken != null && jwtProvider.validateToken(accessToken, ACCESS)) {
				return jwtProvider.getClaim(accessToken, ACCESS);
			} else {
				return new UserInfo();
			}
		}

		throw new NotFoundException(NOT_FOUND_REQUEST);
	}

}
