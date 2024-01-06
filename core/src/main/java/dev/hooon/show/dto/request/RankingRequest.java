package dev.hooon.show.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RankingRequest(
	@NotEmpty
	String category,
	@NotEmpty
	String period
) {
}
