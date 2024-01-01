package dev.hooon.user.application;

import static dev.hooon.user.exception.UserErrorCode.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.auth.entity.EncryptHelper;
import dev.hooon.user.domain.entity.User;
import dev.hooon.user.dto.request.UserJoinRequest;
import dev.hooon.common.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UserService 테스트]")
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private EncryptHelper encryptHelper;
	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("[사용자 ID로 사용자를 성공적으로 조회한다]")
	void getUserByIdSuccessTest() {
		// given
		Long userId = 1L;
		User mockUser = new User(/* 사용자 정보 */);
		given(userRepository.findById(userId))
			.willReturn(Optional.of(mockUser));

		// when
		User foundUser = userService.getUserById(userId);

		// then
		assertThat(foundUser).isNotNull().isEqualTo(mockUser);
	}

	@Test
	@DisplayName("[사용자 ID로 사용자 조회에 실패한다 - 사용자 없음]")
	void getUserByIdFailTest() {
		// given
		Long userId = 1L;
		given(userRepository.findById(userId))
			.willReturn(Optional.empty());

		// when/then
		assertThatThrownBy(() -> userService.getUserById(userId))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("[회원가입을 성공한다]")
	void joinSuccessTest() {
		// given
		UserJoinRequest request = new UserJoinRequest(
			"test@example.com", "testname", "testpassword"
		);
		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.empty());
		given(encryptHelper.encrypt(request.password()))
			.willReturn("encryptedPassword");

		// when
		Long userId = userService.join(request);

		// then
		assertThat(userId).isNotNull();
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("[회원가입을 실패한다 - 이메일 중복]")
	void joinFailTest() {
		// given
		UserJoinRequest request = new UserJoinRequest(
			"test@example.com", "testname", "testpassword"
		);
		given(userRepository.findByEmail(request.email()))
			.willReturn(Optional.of(new User()));

		// when & then
		assertThatThrownBy(() -> userService.join(request))
			.isInstanceOf(ValidationException.class)
			.hasMessageContaining(DUPLICATED_EMAIL.getMessage());
		verify(userRepository, never()).save(any(User.class));
	}
}
