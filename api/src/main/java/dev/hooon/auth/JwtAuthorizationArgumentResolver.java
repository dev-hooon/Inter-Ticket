package dev.hooon.auth;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static dev.hooon.auth.exception.AuthErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.auth.domain.entity.UserInfo;
import dev.hooon.auth.exception.AuthException;
import dev.hooon.user.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

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
			String AUTHORIZATION = "Authorization";
			String token = httpServletRequest.getHeader(AUTHORIZATION);

			// 토큰이 있을 때
			if (token != null && !token.trim().isEmpty() &&
				jwtProvider.validateToken(token, ACCESS)) {
				return jwtProvider.getClaim(token, ACCESS);
			}

			// 토큰은 없지만 인증이 필수가 아닌 경우 기본 객체 리턴
			JwtAuthorization annotation = parameter.getParameterAnnotation(JwtAuthorization.class);
			if (annotation != null && !annotation.required()) {
				return new UserInfo();
			}
		}

		// 토큰이 없고 인증이 필수인 경우 에러
		throw new AuthException(NOT_PERMITTED_USER);
	}

}
