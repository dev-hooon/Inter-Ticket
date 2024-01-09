package dev.hooon.auth;

import static dev.hooon.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.hooon.auth.application.JwtProvider;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.common.support.ApiTestSupport;
import jakarta.servlet.http.HttpServletRequest;

@DisplayName("[JwtAuthorizationArgumentResolver 테스트]")
class JwtAuthorizationArgumentResolverTest extends ApiTestSupport {

	@InjectMocks
	private JwtAuthorizationArgumentResolver resolver;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private MethodParameter parameter;
	@Mock
	private ModelAndViewContainer mavContainer;
	private MockHttpServletRequest mockHttpServletRequest;
	@Mock
	private NativeWebRequest webRequest;
	@Mock
	private WebDataBinderFactory binderFactory;

	@BeforeEach
	void setUp() {
		mockHttpServletRequest = new MockHttpServletRequest();
		resolver = new JwtAuthorizationArgumentResolver(jwtProvider);
	}

	@Test
	@DisplayName("[토큰이 유효하면 claim 의 userId를 반환한다]")
	void shouldResolveArgumentWithValidToken() {
		// given
		Long userId = 123L;
		mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "validToken");
		when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(mockHttpServletRequest);
		when(jwtProvider.getClaim("validToken")).thenReturn(userId);

		// when
		Object o = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

		// then
		assertThat(o).isEqualTo(userId);
	}

	@Test
	@DisplayName("[요청이 없으면 예외를 던진다]")
	void shouldThrowNotFoundExceptionWhenRequestMissing() {
		// given
		when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(null);

		// when + then
		assertThatThrownBy(() -> resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(NOT_FOUND_REQUEST.getMessage());
	}

}
