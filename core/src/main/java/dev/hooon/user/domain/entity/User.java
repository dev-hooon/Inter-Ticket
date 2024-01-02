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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_email", nullable = false, unique = true)
	private String email;

	@Column(name = "user_name", nullable = false)
	private String name;

	@Enumerated(STRING)
	@Column(name = "user_role", nullable = false)
	private UserRole userRole;

	// 테스트용 AllArgsConstructor
	public User(String email, String name, UserRole userRole) {
		this.email = email;
		this.name = name;
		this.userRole = userRole;
	}
}
