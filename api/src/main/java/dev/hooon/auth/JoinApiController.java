package dev.hooon.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.user.application.UserService;
import dev.hooon.user.dto.request.UserJoinRequest;
import dev.hooon.user.dto.response.UserJoinResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinApiController {

	private final UserService userService;

	@PostMapping("/api/auth/new")
	public ResponseEntity<UserJoinResponse> join(
		final @Valid @RequestBody UserJoinRequest userJoinRequest
	) {
		Long userId = userService.join(userJoinRequest);
		return ResponseEntity.ok(new UserJoinResponse(userId));
	}

}
