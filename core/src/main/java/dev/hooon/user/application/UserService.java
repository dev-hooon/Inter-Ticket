package dev.hooon.user.application;

import static dev.hooon.user.exception.UserErrorCode.*;

import org.springframework.stereotype.Service;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID));
	}
}
