package dev.hooon.show.dto;

import java.util.List;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse.AvailableDate;
import dev.hooon.show.dto.response.PlaceDetailsInfo;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
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

	public static ShowDetailsInfoResponse toShowDetailInfoResponse(Show show) {
		return new ShowDetailsInfoResponse(
			show.getName(),
			show.getShowPeriod().getStartDate(),
			show.getShowPeriod().getEndDate(),
			show.getShowTime().getTotalMinutes(),
			show.getShowTime().getIntermissionMinutes(),
			show.getShowAgeLimit(),
			new PlaceDetailsInfo(
				show.getPlace().getName(),
				show.getPlace().getContactInfo(),
				show.getPlace().getAddress(),
				show.getPlace().getPlaceUrl()
			)
		);
	}
}
