package dev.hooon.auth.application;

import static dev.hooon.auth.exception.AuthErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.entity.BlacklistToken;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.domain.repository.BlacklistRepository;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.entity.UserRole;

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
	@Mock
	private EncryptHelper encryptHelper;
	@Mock
	private BlacklistRepository blacklistRepository;

	@Test
	@DisplayName("[로그인 성공 시 토큰을 발급한다]")
	void loginSuccessTest() {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "password");
		User user = User.testUser(1L, "user@example.com", "name", "password", UserRole.BUYER);

		when(userService.getUserByEmail(authRequest.email())).thenReturn(user);
		when(userService.getUserById(1L)).thenReturn(user);
		when(encryptHelper.isMatch(anyString(), anyString())).thenReturn(true);
		Auth anyAuth = new Auth();
		when(authRepository.findByUserId(user.getId())).thenReturn(Optional.of(anyAuth));
		when(jwtProvider.createAccessToken(anyLong())).thenReturn("access-token");
		when(jwtProvider.createRefreshToken(anyLong())).thenReturn("refresh-token");

		// when
		AuthResponse authResponse = authService.login(authRequest);

		// then
		assertEquals("access-token", authResponse.accessToken());
		assertEquals("refresh-token", authResponse.refreshToken());
	}

	@Test
	@DisplayName("로그인 실패 시 예외를 던진다")
	void loginFailTest() {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "wrong-password");
		User user = User.testUser(1L, "user@example.com", "name", "password", UserRole.BUYER);

		when(userService.getUserByEmail(authRequest.email())).thenReturn(user);
		when(userService.getUserById(1L)).thenReturn(user);
		when(encryptHelper.isMatch(anyString(), anyString())).thenReturn(false);

		// when & then
		assertThrows(NotFoundException.class, () -> authService.login(authRequest));
	}

	@Test
	@DisplayName("로그아웃 성공 시 블랙리스트에 토큰을 추가한다")
	void logoutSuccessTest() {
		// given
		Long userId = 1L;
		Auth auth = Auth.of(userId, "refresh-token");
		when(authRepository.findByUserId(userId)).thenReturn(Optional.of(auth));

		// when
		authService.logout(userId);

		// then
		verify(blacklistRepository).save(any(BlacklistToken.class));
	}

	@Test
	@DisplayName("로그아웃 실패 시 NotFoundException을 던진다")
	void logoutFailTest() {
		// given
		Long userId = 1L;
		when(authRepository.findByUserId(userId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> authService.logout(userId))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(NOT_FOUND_USER_ID.getMessage());
	}
}
