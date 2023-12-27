package dev.hooon.show.dto.response.seats;

import java.util.List;

import lombok.Getter;

@Getter
public class ShowSeatsResponse {

	private List<SeatsInfoDto> seatsInfo;

	public ShowSeatsResponse(List<SeatsInfoDto> seatsInfo) {
		this.seatsInfo = seatsInfo;
	}
}
