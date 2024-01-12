package dev.hooon.auth.domain.entity;

import static jakarta.persistence.GenerationType.*;

import dev.hooon.common.entity.TimeBaseEntity;
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
@Table(name = "blacklist_token_table")
public class BlacklistToken extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "blacklist_token_id")
	private Long id;

	@Column(name = "blacklist_token_refresh_token", nullable = false, unique = true)
	private String refreshToken;

	private BlacklistToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static BlacklistToken of(String refreshToken) {
		return new BlacklistToken(refreshToken);
	}
}
