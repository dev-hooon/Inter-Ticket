package dev.hooon.show.dto.response;

import dev.hooon.show.domain.entity.Show;

public record ShowInfoResponse(
	String showName,
	String placeName
) {
	public static ShowInfoResponse of(
		Show show
	) {
		return new ShowInfoResponse(show.getName(), show.getPlace().getName());
	}
}
