package dev.hooon.auth;

import static dev.hooon.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.HandlerMethod;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.auth.exception.AuthException;
import dev.hooon.common.support.ApiTestSupport;
import jakarta.servlet.http.HttpServletResponse;

@DisplayName("[JwtInterceptor 테스트]")
class JwtInterceptorTest extends ApiTestSupport {

	@InjectMocks
	private JwtInterceptor jwtInterceptor;
	@Mock
	private JwtProvider jwtProvider;
	private MockHttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HandlerMethod handlerMethod;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
	}

	@Test
	@DisplayName("[토큰이 없고 @NoAuth 있으면 -> 통과]")
	void shouldPassWithoutTokenWhenNoAuthAnnotationIsPresent() {
		// given, when
		when(handlerMethod.getMethodAnnotation(NoAuth.class))
			.thenReturn(mock(NoAuth.class));
		// then
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod)).isTrue();
	}

	@Test
	@DisplayName("[토큰이 유효하다면 -> 통과]")
	void shouldPassWithValidToken() {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, "validToken");
		// when
		doNothing().when(jwtProvider).validateToken("validToken");
		// then
		assertThat(jwtInterceptor.preHandle(request, response, handlerMethod)).isTrue();
	}

	@Test
	@DisplayName("[토큰이 없고 @NoAuth 없으면 -> 실패]")
	void shouldThrowExceptionWhenTokenIsMissing() {
		assertThatThrownBy(
			() -> jwtInterceptor.preHandle(request, response, handlerMethod)
		)
			.isInstanceOf(AuthException.class)
			.hasMessageContaining(NOT_INCLUDE_ACCESS_TOKEN.getMessage());
	}

	@Test
	@DisplayName("[토큰이 유효하지 않고 @NoAuth 없으면 -> 실패]")
	void shouldThrowExceptionWithInvalidToken() {
		// given
		request.addHeader(HttpHeaders.AUTHORIZATION, "invalidToken");
		doThrow(new AuthException(TOKEN_EXPIRED))
			.when(jwtProvider).validateToken("invalidToken");

		// when, then
		assertThatThrownBy(
			() -> jwtInterceptor.preHandle(request, response, handlerMethod)
		)
			.isInstanceOf(AuthException.class)
			.hasMessageContaining(TOKEN_EXPIRED.getMessage());
	}

}
