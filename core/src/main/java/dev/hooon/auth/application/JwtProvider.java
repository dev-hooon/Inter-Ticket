package dev.hooon.auth.application;

import static dev.hooon.auth.exception.AuthErrorCode.*;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.hooon.auth.domain.entity.Auth;
import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final AuthRepository authRepository;

	@Value(value = "${jwt.secret}")
	private String secretKey;

	@Value(value = "${jwt.token-validity-in-seconds}")
	private int tokenValidSeconds;

	private Key key;
	private static final String USER_ID = "userId";

	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createAccessToken(Long userId) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim(USER_ID, userId)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidSeconds))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String createRefreshToken(Long userId) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim(USER_ID, userId)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidSeconds * 30L))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String[] createTokensWhenLogin(Long userId){
		String refreshToken = createRefreshToken(userId);
		String accessToken = createAccessToken(userId);
		Optional<Auth> auth = authRepository.findByUserId(userId);

		auth.ifPresentOrElse(
			(none) -> authRepository.updateRefreshToken(auth.get().getId(), refreshToken),
			() -> {
				Auth newAuth = Auth.of(userId, refreshToken);
				authRepository.save(newAuth);
			}
		);

		return new String[] {refreshToken, accessToken};
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
			throw new AuthException(INVALID_TOKEN);
		}
	}

	public Long getClaim(String token) {
		Claims claimsBody = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.valueOf((Integer)claimsBody.getOrDefault(USER_ID, 0L));
	}

}
