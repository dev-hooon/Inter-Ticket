package dev.hooon.auth.application;

import static dev.hooon.auth.exception.AuthErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.entity.BlacklistToken;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.domain.repository.BlacklistRepository;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.auth.exception.AuthException;
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
	private final BlacklistRepository blacklistRepository;

	private Auth getAuthByRefreshToken(String refreshToken) {
		return authRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_REFRESH_TOKEN));
	}

	private AuthResponse saveAuth(Long userId) {
		String refreshToken = jwtProvider.createRefreshToken(userId);
		String accessToken = jwtProvider.createAccessToken(userId);
		Optional<Auth> auth = authRepository.findByUserId(userId);

		auth.ifPresentOrElse(
			existingAuth -> authRepository.updateRefreshToken(existingAuth.getId(), refreshToken),
			() -> {
				Auth newAuth = Auth.of(userId, refreshToken);
				authRepository.save(newAuth);
			}
		);

		return AuthResponse.of(refreshToken, accessToken);
	}

	@Transactional
	public AuthResponse login(AuthRequest authRequest) {
		Long userId = userService.getUserByEmail(authRequest.email()).getId();
		AuthResponse authResponse = saveAuth(userId);
		String plainPassword = authRequest.password();
		String hashedPassword = userService.getUserById(userId).getPassword();

		if (encryptHelper.isMatch(plainPassword, hashedPassword)) {
			return authResponse;
		}
		throw new NotFoundException(FAILED_LOGIN_BY_ANYTHING);
	}

	@Transactional
	public void logout(Long userId) {
		authRepository.findByUserId(userId).ifPresentOrElse(
			auth ->
				blacklistRepository.save(BlacklistToken.of(auth.getRefreshToken())),
			() -> {
				throw new NotFoundException(NOT_FOUND_USER_ID);
			}
		);
	}

	public String createAccessTokenByRefreshToken(String refreshToken) {
		boolean isBlacklisted = blacklistRepository.existsByRefreshToken(refreshToken);
		if (isBlacklisted) {
			throw new AuthException(BLACKLISTED_TOKEN);
		}

		Auth auth = getAuthByRefreshToken(refreshToken);
		Long userId = userService.getUserById(auth.getUserId()).getId();
		return jwtProvider.createAccessToken(userId);
	}

}
