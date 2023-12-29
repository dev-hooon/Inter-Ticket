package dev.hooon.show.dto.response.seats;

import java.util.List;

import lombok.Getter;

@Getter
public class ShowSeatsResponse {

	private List<SeatsInfoResponse> seatsInfo;

	public ShowSeatsResponse(List<SeatsInfoResponse> seatsInfo) {
		this.seatsInfo = seatsInfo;
	}
}
