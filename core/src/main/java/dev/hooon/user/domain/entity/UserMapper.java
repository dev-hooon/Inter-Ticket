package dev.hooon.user.domain.entity;

import static dev.hooon.user.domain.entity.UserRole.*;

import dev.hooon.user.dto.request.UserJoinRequestDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class UserMapper {

	public static User toBuyerUser(UserJoinRequestDto userJoinRequestDto) {
		return User.of(
			userJoinRequestDto.email(),
			userJoinRequestDto.name(),
			userJoinRequestDto.password(),
			BUYER
		);
	}
}
