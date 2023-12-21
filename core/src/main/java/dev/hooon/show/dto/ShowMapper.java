package dev.hooon.show.dto;

import java.util.List;

import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse.AvailableDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowMapper {

	public static AbleBookingDateRoundResponse toSeatDateRoundResponse(List<SeatDateRoundDto> seatDateRoundDtoList) {
		List<AvailableDate> availableDates = seatDateRoundDtoList.stream()
			.map(seatDateRoundDto -> new AvailableDate(
				seatDateRoundDto.showDate(),
				seatDateRoundDto.round(),
				seatDateRoundDto.startTime()
			)).toList();

		return new AbleBookingDateRoundResponse(availableDates);
	}
}
