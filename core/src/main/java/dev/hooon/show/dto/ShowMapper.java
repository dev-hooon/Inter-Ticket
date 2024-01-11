package dev.hooon.show.dto;

import static dev.hooon.show.dto.response.RankingResponse.*;

import java.time.LocalDateTime;
import java.util.List;

import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.query.ShowStatisticDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse.AvailableDate;
import dev.hooon.show.dto.response.PlaceDetailsInfo;
import dev.hooon.show.dto.response.RankingResponse;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import dev.hooon.show.dto.response.ShowInfoResponse;
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

	public static RankingResponse toRankingResponse(
		List<ShowStatisticDto> showStatistic,
		LocalDateTime startAt,
		LocalDateTime endAt
	) {
		List<RankingShowInfo> rankingShowInfos = showStatistic.stream()
			.map(dto -> new RankingShowInfo(
				dto.showName(),
				dto.placeName(),
				dto.showStartDate(),
				dto.showEndDate(),
				dto.totalTicketCount()
			)).toList();

		return new RankingResponse(
			startAt,
			endAt,
			rankingShowInfos
		);
	}

	public static ShowInfoResponse toShowInfoResponse(Show show) {
		return new ShowInfoResponse(
			show.getName(),
			show.getPlace().getName()
		);
	}
}
