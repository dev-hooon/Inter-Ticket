package dev.hooon.user.domain.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

	private User(
		String email,
		String name,
		String password,
		UserRole userRole
	) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.userRole = userRole;
	}

	public static User of(
		String email,
		String name,
		String password,
		UserRole userRole
	) {
		return new User(email, name, password, userRole);
	}
}
