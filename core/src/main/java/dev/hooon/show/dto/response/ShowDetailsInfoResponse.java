package dev.hooon.show.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ShowDetailsInfoResponse(
	String showName,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate showStartDate,
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDate showEndDate,
	int showTime,
	int showIntermissionTime,
	String showAgeLimit,
	PlaceDetailsInfo placeDetailsInfo
) {
}
