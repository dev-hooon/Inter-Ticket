package dev.hooon.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.hooon.user.application.UserService;
import dev.hooon.user.dto.request.UserJoinRequestDto;
import dev.hooon.user.dto.response.UserJoinResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@PostMapping("/api/users/new")
	public ResponseEntity<UserJoinResponseDto> join(
		final @Valid @RequestBody UserJoinRequestDto userJoinRequestDto
	) {
		Long userId = userService.join(userJoinRequestDto);
		return ResponseEntity.ok(new UserJoinResponseDto(userId));
	}

}
