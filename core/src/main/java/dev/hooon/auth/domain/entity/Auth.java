package dev.hooon.auth.domain.entity;

import static dev.hooon.common.exception.CommonValidationError.*;
import static jakarta.persistence.GenerationType.*;

import org.springframework.util.Assert;

import dev.hooon.user.domain.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auth_table")
public class Auth {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "auth_id")
	private Long id;

	@Column(name = "auth_user_id", nullable = false, unique = true)
	private Long userId;

	@Column(name = "auth_refresh_token", nullable = false, unique = true)
	private String refreshToken;

	private Auth(Long userId, String refreshToken) {
		this.userId = userId;
		this.refreshToken = refreshToken;
	}

	public static Auth of(Long userId, String refreshToken) {
		return new Auth(userId, refreshToken);
	}
}
