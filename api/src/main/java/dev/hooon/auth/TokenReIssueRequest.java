package dev.hooon.auth;


public record TokenReIssueRequest(
	String refreshToken
) {
}
