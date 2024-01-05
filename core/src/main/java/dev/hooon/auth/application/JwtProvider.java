package dev.hooon.auth.application;

import static dev.hooon.auth.domain.entity.TokenType.*;
import static dev.hooon.auth.exception.AuthErrorCode.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.stereotype.Component;

import dev.hooon.auth.domain.entity.TokenType;
import dev.hooon.auth.domain.entity.UserInfo;
import dev.hooon.auth.exception.AuthException;
import dev.hooon.user.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

	public String createToken(User user, TokenType tokenType) {
		Claims claims = Jwts.claims();
		claims.put("id", user.getId());
		claims.put("email", user.getEmail());
		claims.put("name", user.getName());
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenType.getValidTime()))
			.signWith(
				Keys.hmacShaKeyFor(tokenType.getSecretKey().getBytes(StandardCharsets.UTF_8)),
				SignatureAlgorithm.HS256
			)
			.compact();
	}

	public boolean validateToken(String token, TokenType tokenType) {
		try {
			return !Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(tokenType.getSecretKey().getBytes(StandardCharsets.UTF_8)))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration().before(new Date());
		} catch (Exception e) {
			if(tokenType.equals(ACCESS)){
				throw new AuthException(INVALID_ACCESS_TOKEN);
			}else{
				throw new AuthException(INVALID_REFRESH_TOKEN);
			}
		}
	}

	public UserInfo getClaim(String token, TokenType tokenType) {
		Claims claimsBody = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(tokenType.getSecretKey().getBytes(StandardCharsets.UTF_8)))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return UserInfo.of(
			Long.valueOf((Integer)claimsBody.getOrDefault("id", 0L)),
			claimsBody.getOrDefault("email", "").toString(),
			claimsBody.getOrDefault("name", "").toString()
		);
	}

}
