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

	@Test
	@DisplayName("[USER 를 생성하는데 실패한다 - By Blank]")
	void ofBuyerFailTest() {
		// given
		String email = "test@example.com";
		String name = "testname";
		String password = "testpassword";

		// when & then
		assertThatThrownBy(() -> {
			User.ofBuyer(null, name, password);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotNullMessage("User", "email"));

		assertThatThrownBy(() -> {
			User.ofBuyer("", name, password);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotEmptyPostfix("User", "email"));

		assertThatThrownBy(() -> {
			User.ofBuyer(email, null, password);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotNullMessage("User", "name"));

		assertThatThrownBy(() -> {
			User.ofBuyer(email, "", password);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotEmptyPostfix("User", "name"));

		assertThatThrownBy(() -> {
			User.ofBuyer(email, name, null);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotNullMessage("User", "password"));

		assertThatThrownBy(() -> {
			User.ofBuyer(email, name, "");
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(getNotEmptyPostfix("User", "password"));
	}
}
