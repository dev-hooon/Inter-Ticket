package dev.hooon.auth.application;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;

@DisplayName("[AuthService 테스트]")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;
	@Mock
	private AuthRepository authRepository;
	@Mock
	private UserService userService;
	@Mock
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("[로그인 성공 시 토큰을 발급한다]")
	void createTokensWhenLoginSuccessTest() {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "password");
		User user = User.ofBuyer("user@example.com", "name", "password");

		when(userService.getUserByEmail(authRequest.email())).thenReturn(user);
		when(jwtProvider.createToken(user, ACCESS)).thenReturn("access-token");
		when(jwtProvider.createToken(user, REFRESH)).thenReturn("refresh-token");

		// when
		AuthResponse authResponse = authService.login(authRequest);

		// then
		assertEquals("access-token", authResponse.accessToken());
		assertEquals("refresh-token", authResponse.refreshToken());
		verify(authRepository).save(any(Auth.class));
	}

	@Test
	@DisplayName("로그인 실패 시 예외를 던진다")
	void createTokensWhenLoginFailTest() {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "wrong-password");
		User user = User.ofBuyer("user@example.com", "name", "password");

		when(userService.getUserByEmail(authRequest.email())).thenReturn(user);

		// when & then
		assertThrows(NotFoundException.class, () -> authService.login(authRequest));
	}
}
