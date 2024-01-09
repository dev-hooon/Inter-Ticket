package dev.hooon.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.user.application.UserService;
import dev.hooon.user.dto.request.UserJoinRequest;
import dev.hooon.user.dto.response.UserJoinResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@NoAuth
	@PostMapping("/api/users")
	public ResponseEntity<UserJoinResponse> join(
		final @Valid @RequestBody UserJoinRequest userJoinRequest
	) {
		Long userId = userService.join(userJoinRequest);
		return ResponseEntity.ok(new UserJoinResponse(userId));
	}

}
