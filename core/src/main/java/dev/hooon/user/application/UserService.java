package dev.hooon.user.application;

import static dev.hooon.user.exception.UserErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.domain.entity.UserMapper;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.user.dto.request.UserJoinRequestDto;
import dev.hooon.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_BY_ID));
	}

	@Transactional
	public Long join(UserJoinRequestDto userJoinRequestDto) {
		validateDuplicateUser(userJoinRequestDto);
		User user = UserMapper.toBuyerUser(userJoinRequestDto);
		return userRepository.save(user);
	}

	private void validateDuplicateUser(UserJoinRequestDto userJoinRequestDto) {
		List<User> findUsers = userRepository.findByName(userJoinRequestDto.name());
		if (!findUsers.isEmpty()) {
			log.info(DUPLICATED_USER.getMessage());
			throw new UserException(DUPLICATED_USER);
		}
	}
}
