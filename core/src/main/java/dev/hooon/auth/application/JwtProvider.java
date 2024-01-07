package dev.hooon.auth.application;

import static dev.hooon.auth.exception.AuthErrorCode.*;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.hooon.auth.domain.repository.AuthRepository;
import dev.hooon.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final AuthRepository authRepository;
	private final String secretKey;
	private final int tokenValidSeconds;
	private final Key key;
	private static final String USER_ID = "userId";

	@Autowired
	public JwtProvider(
		AuthRepository authRepository,
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.token-validity-in-seconds}") int tokenValidSeconds
	) {
		this.authRepository = authRepository;
		this.secretKey = secretKey;
		this.tokenValidSeconds = tokenValidSeconds;

		byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
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
