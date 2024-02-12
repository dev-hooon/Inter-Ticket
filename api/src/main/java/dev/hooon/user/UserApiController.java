package dev.hooon.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.auth.annotation.NoAuth;
import dev.hooon.user.application.UserService;
import dev.hooon.user.dto.request.UserJoinRequest;
import dev.hooon.user.dto.response.UserJoinResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User API")
@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@NoAuth
	@PostMapping("/api/users")
	@Operation(summary = "회원가입 API", description = "회원가입을 한다")
	@ApiResponse(responseCode = "200", useReturnTypeSchema = true)
	public ResponseEntity<UserJoinResponse> join(
		final @Valid @RequestBody UserJoinRequest userJoinRequest
	) {
		Long userId = userService.join(userJoinRequest);
		return ResponseEntity.ok(new UserJoinResponse(userId));
	}

	//test
}
