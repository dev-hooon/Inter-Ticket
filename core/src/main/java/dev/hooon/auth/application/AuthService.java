
package dev.hooon.auth.application;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static dev.hooon.auth.exception.AuthErrorCode.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.entity.TokenType;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
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

	private Map<TokenType, String> createTokensWhenLogin(User user) {
		Map<TokenType, String> tokens = new EnumMap<>(TokenType.class);
		String accessToken = jwtProvider.createToken(user, ACCESS);
		String refreshToken = jwtProvider.createToken(user, REFRESH);
		tokens.put(ACCESS, accessToken);
		tokens.put(REFRESH, refreshToken);

		Long userId = user.getId();
		Optional<Auth> auth = authRepository.findByUserId(userId);

		auth.ifPresentOrElse(
			(none) -> authRepository.updateRefreshToken(auth.get().getId(), refreshToken),
			() -> {
				Auth newAuth = Auth.of(userId, refreshToken);
				authRepository.save(newAuth);
			}
		);

		return tokens;
	}

	@Transactional
	public AuthResponse login(AuthRequest authRequest) {
		User user = userService.getUserByEmail(authRequest.email());
		Map<TokenType, String> tokens = createTokensWhenLogin(user);

		String plainPassword = authRequest.password();
		String hashedPassword = userService.getUserById(user.getId()).getPassword();

		if (encryptHelper.isMatch(plainPassword, hashedPassword)) {
			return AuthResponse.of(
				user.getName(),
				user.getEmail(),
				tokens.get(ACCESS),
				tokens.get(REFRESH)
			);
		}
		throw new NotFoundException(FAILED_LOGIN_BY_ANYTHING);
	}

	public String generateAccessTokenFromRefreshToken(String refreshToken) {
		Auth auth = getAuthByRefreshToken(refreshToken);
		User user = userService.getUserById(auth.getUserId());
		return jwtProvider.createToken(user, ACCESS);
	}

}
