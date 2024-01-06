package dev.hooon.common.fixture;

import dev.hooon.user.domain.entity.User;

public final class UserFixture {

	private UserFixture() {
	}

	public static User getUser() {
		return User.ofBuyer("hello123@naver.com", "name", "password");
	}
}
