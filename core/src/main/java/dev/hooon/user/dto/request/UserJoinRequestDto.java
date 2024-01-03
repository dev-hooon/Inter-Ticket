package dev.hooon.user.dto.request;

public record UserJoinRequestDto(
	String email,
	String password,
	String name
) {}
