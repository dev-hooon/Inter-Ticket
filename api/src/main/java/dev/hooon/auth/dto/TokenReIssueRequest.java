package dev.hooon.auth.dto;


public record TokenReIssueRequest(
	String refreshToken
) {
}
