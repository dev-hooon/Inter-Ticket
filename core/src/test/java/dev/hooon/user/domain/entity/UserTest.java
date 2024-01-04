package dev.hooon.user.domain.entity;

import static dev.hooon.common.exception.CommonValidationError.*;
import static dev.hooon.user.domain.entity.UserRole.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[User 테스트]")
class UserTest {

	@Test
	@DisplayName("[USER 를 생성하는데 성공한다]")
	void ofBuyerSuccessTest() {
		// given
		String email = "test@example.com";
		String name = "testname";
		String password = "testpassword";

		// when
		User user = User.ofBuyer(email, name, password);

		// then
		assertThat(user).isNotNull();
		assertThat(email).isEqualTo(user.getEmail());
		assertThat(name).isEqualTo(user.getName());
		assertThat(password).isEqualTo(user.getPassword());
		assertThat(BUYER).isEqualTo(user.getUserRole());
	}
}
