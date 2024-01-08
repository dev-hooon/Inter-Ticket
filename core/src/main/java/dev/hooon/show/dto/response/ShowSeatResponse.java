package dev.hooon.show.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.hooon.show.domain.entity.seat.SeatStatus;

public record ShowSeatResponse(
	List<SeatsDetailInfo> seatsDetailInfos
) {
	public record SeatsDetailInfo(
		Long id,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate date,
		boolean isSeat,
		@JsonProperty("positionInfo_sector")
		String sector,
		@JsonProperty("positionInfo_row")
		String row,
		@JsonProperty("positionInfo_col")
		int col,
		int price,
		SeatStatus status
	) {
	}
}
