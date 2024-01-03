package dev.hooon.user.dto;

import dev.hooon.user.domain.entity.User;
import dev.hooon.user.dto.request.UserJoinRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class UserMapper {

	public static User toUser(UserJoinRequest userJoinRequest) {
		return User.ofBuyer(
			userJoinRequest.email(),
			userJoinRequest.name(),
			userJoinRequest.password()
		);
	}
}
