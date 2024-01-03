package dev.hooon.auth.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo {
	private Long id;
	private String email;
	private String name;

	public UserInfo(Long id, String email, String name) {
		this.id = id;
		this.email = email;
		this.name = name;
	}

	public static UserInfo of(Long id, String email, String name) {
		return new UserInfo(id, email, name);
	}
}
