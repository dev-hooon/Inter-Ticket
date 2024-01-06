package dev.hooon.show.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RankingResponse(
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime aggregateStartAt,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime aggregateEndAt,
	List<RankingShowInfo> showInfos
) {
	public record RankingShowInfo(
		String showName,
		String placeName,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate showStartDate,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate showEndDate,
		long totalTicketCount
	) {
	}
}
