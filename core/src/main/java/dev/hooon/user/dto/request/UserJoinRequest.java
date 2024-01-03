package dev.hooon.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserJoinRequest(

	@Email
	String email,
	@NotBlank
	String password,
	@NotBlank
	String name
) {}
