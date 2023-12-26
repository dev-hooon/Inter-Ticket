package dev.hooon.user.application;

import static dev.hooon.user.exception.UserErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.user.dto.request.UserJoinRequest;
import dev.hooon.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final EncryptHelper encryptHelper;

	private void validateDuplicateEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new ValidationException(DUPLICATED_EMAIL);
		}
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID));
	}

	@Transactional
	public Long join(UserJoinRequest userJoinRequest) {
		validateDuplicateEmail(userJoinRequest.email());
		String email = userJoinRequest.email();
		String name = userJoinRequest.name();
		String password = userJoinRequest.password();
		String encryptedPassword = encryptHelper.encrypt(password);
		User user = User.ofBuyer(email, name, encryptedPassword);
		return userRepository.save(user);
	}

}
