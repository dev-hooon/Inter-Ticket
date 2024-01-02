package dev.hooon.show.dto;

import java.util.List;

import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;
import dev.hooon.show.dto.response.seats.SeatsDetailResponse;
import dev.hooon.show.dto.response.seats.SeatsInfoResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatMapper {

	public static List<SeatsInfoResponse> toSeatsInfoResponse(List<SeatsInfoDto> seatsInfoDtoList) {
		return seatsInfoDtoList.stream()
			.map(it -> new SeatsInfoResponse(
				it.grade(),
				it.leftSeats(),
				it.price()
			))
			.toList();
	}

	public static List<SeatsDetailResponse> getSeatsDetailResponses(List<SeatsDetailDto> seatsDetailDtoList) {
		return seatsDetailDtoList.stream()
			.map(o -> new SeatsDetailResponse(
				o.id(),
				o.date(),
				o.isSeat(),
				o.sector(),
				o.row(),
				o.col(),
				o.price(),
				o.isBookingAvailable()
			))
			.toList();
	}

}
