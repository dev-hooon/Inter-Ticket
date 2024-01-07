
package dev.hooon.auth.application;

import static dev.hooon.auth.exception.AuthErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.application.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserService userService;
	private final JwtProvider jwtProvider;
	private final AuthRepository authRepository;
	private final EncryptHelper encryptHelper;

	private Auth getAuthByRefreshToken(String refreshToken) {
		return authRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_REFRESH_TOKEN));
	}

	@Transactional
	public AuthResponse login(AuthRequest authRequest) {
		Long userId = userService.getUserByEmail(authRequest.email()).getId();
		String[] tokensWhenLogin = jwtProvider.createTokensWhenLogin(userId);
		String plainPassword = authRequest.password();
		String hashedPassword = userService.getUserById(userId).getPassword();

		if (encryptHelper.isMatch(plainPassword, hashedPassword)) {
			return AuthResponse.of(
				tokensWhenLogin[0],
				tokensWhenLogin[1]
			);
		}
		throw new NotFoundException(FAILED_LOGIN_BY_ANYTHING);
	}

	public String createAccessTokenByRefreshToken(String refreshToken) {
		Auth auth = getAuthByRefreshToken(refreshToken);
		Long userId = userService.getUserById(auth.getUserId()).getId();
		return jwtProvider.createAccessToken(userId);
	}

}
