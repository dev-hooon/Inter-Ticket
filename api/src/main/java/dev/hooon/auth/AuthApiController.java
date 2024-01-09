package dev.hooon.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.auth.application.AuthService;
import dev.hooon.auth.dto.TokenReIssueRequest;
import dev.hooon.auth.dto.request.AuthRequest;

import dev.hooon.auth.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

	private final AuthService authService;

	@NoAuth
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
		@Valid @RequestBody AuthRequest authRequest
	) {
		AuthResponse authResponse = authService.login(authRequest);
		return ResponseEntity.ok(authResponse);
	}

	@NoAuth
	@PostMapping("/token")
	public ResponseEntity<String> reIssueAccessToken(
		@RequestBody TokenReIssueRequest tokenReIssueRequest
	) {
		String accessToken = authService.createAccessTokenByRefreshToken(tokenReIssueRequest.refreshToken());
		return ResponseEntity.ok(accessToken);
	}
}
