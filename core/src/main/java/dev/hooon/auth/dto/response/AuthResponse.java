package dev.hooon.auth.dto.response;

public record AuthResponse(
	String name,
	String email,
	String accessToken,
	String refreshToken
) {
	public static AuthResponse of(
		String name,
		String email,
		String accessToken,
		String refreshToken
	) {
		return new AuthResponse(name, email, accessToken, refreshToken);
	}
}
