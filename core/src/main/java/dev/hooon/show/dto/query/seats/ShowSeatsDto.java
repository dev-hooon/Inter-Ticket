package dev.hooon.show.dto.query.seats;

import java.util.List;

public record ShowSeatsDto(
	List<SeatsInfoDto> seatsInfo
) {
}
