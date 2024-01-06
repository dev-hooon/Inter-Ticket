package dev.hooon.auth.dto.response;

public record AuthResponse(
	String refreshToken,
	String accessToken
) {
	public static AuthResponse of(
		String refreshToken,
		String accessToken
	) {
		return new AuthResponse(refreshToken, accessToken);
	}
}
