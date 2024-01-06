package dev.hooon.user.domain.entity;

import static dev.hooon.common.exception.CommonValidationError.*;
import static dev.hooon.user.domain.entity.UserRole.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

import org.springframework.util.Assert;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_table")
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_email", nullable = false, unique = true)
	private String email;

	@Column(name = "user_name", nullable = false)
	private String name;

	@Column(name = "user_password", nullable = false)
	private String password;

	@Enumerated(STRING)
	@Column(name = "user_role", nullable = false)
	private UserRole userRole;

	private void validateUser(String email, String name, String password, UserRole userRole) {
		Assert.hasText(email, getNotEmptyPostfix("User", "email"));
		Assert.hasText(name, getNotEmptyPostfix("User", "name"));
		Assert.hasText(password, getNotEmptyPostfix("User", "password"));
		Assert.hasText(String.valueOf(userRole), getNotEmptyPostfix("User", "userRole"));
		Assert.notNull(userRole, getNotNullMessage("User", "userRole"));
	}

	private User(
		String email,
		String name,
		String password,
		UserRole userRole
	) {
		validateUser(email, name, password, userRole);
		this.email = email;
		this.name = name;
		this.password = password;
		this.userRole = userRole;
	}

	// 테스트를 위한 생성자
	private User(
		Long id,
		String email,
		String name,
		String password,
		UserRole userRole
	) {
		validateUser(email, name, password, userRole);
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.userRole = userRole;
	}

	public static User ofBuyer(
		String email,
		String name,
		String password
	) {
		return new User(email, name, password, BUYER);
	}

	// 테스트를 위한 팩토리 메서드
	public static User ofBuyer(
		Long id,
		String email,
		String name,
		String password
	) {
		return new User(id, email, name, password, BUYER);
	}
}
