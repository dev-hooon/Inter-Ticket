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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API")
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

	private final AuthService authService;

	@NoAuth
	@PostMapping("/login")
	@Operation(summary = "로그인 API", description = "로그인을 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<AuthResponse> login(
		@Valid @RequestBody AuthRequest authRequest
	) {
		AuthResponse authResponse = authService.login(authRequest);
		return ResponseEntity.ok(authResponse);
	}

	@NoAuth
	@PostMapping("/token")
	@Operation(summary = "토큰 재발급 API", description = "토큰을 재발급한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<String> reIssueAccessToken(
		@RequestBody TokenReIssueRequest tokenReIssueRequest
	) {
		String accessToken = authService.createAccessTokenByRefreshToken(tokenReIssueRequest.refreshToken());
		return ResponseEntity.ok(accessToken);
	}
}
