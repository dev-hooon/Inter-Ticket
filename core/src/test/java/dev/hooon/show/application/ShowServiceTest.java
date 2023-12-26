package dev.hooon.show.application;

import static dev.hooon.show.domain.entity.ShowCategory.*;
import static dev.hooon.show.dto.response.AbleBookingDateRoundResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.common.exception.NotFoundException;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.ShowPeriod;
import dev.hooon.show.domain.entity.ShowTime;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.ShowMapper;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;
import dev.hooon.show.dto.response.ShowDetailsInfoResponse;
import dev.hooon.show.exception.ShowErrorCode;

@DisplayName("[ShowService 테스트]")
@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

	@InjectMocks
	private ShowService showService;
	@Mock
	private SeatRepository seatRepository;

	@Mock
	private ShowRepository showRepository;

	@Test
	@DisplayName("[show_id 를 통해 예매 가능한 날짜와 회차 정보를 조회한다]")
	void getAbleBookingDateRoundInfoTest() {
		//given
		List<SeatDateRoundDto> seatDateRoundDtoList = List.of(
			new SeatDateRoundDto(LocalDate.now(), 1, LocalTime.now()),
			new SeatDateRoundDto(LocalDate.now().plusDays(1), 1, LocalTime.now())
		);

		given(seatRepository.findSeatDateRoundInfoByShowId(1L))
			.willReturn(seatDateRoundDtoList);

		//when
		AbleBookingDateRoundResponse result = showService.getAbleBookingDateRoundInfo(1L);

		//then
		List<AvailableDate> availableDates = result.availableDates();
		assertThat(availableDates).hasSameSizeAs(seatDateRoundDtoList);

		for (int i = 0; i < availableDates.size(); i++) {
			AvailableDate actual = availableDates.get(i);
			SeatDateRoundDto expected = seatDateRoundDtoList.get(i);
			assertAll(
				() -> assertThat(actual.showDate()).isEqualTo(expected.showDate()),
				() -> assertThat(actual.round()).isEqualTo(expected.round()),
				() -> assertThat(actual.startTime()).isEqualTo(expected.startTime())
			);
		}
	}

	@Test
	@DisplayName("[show_id 를 통해 공연의 세부 정보를 조회할 수 있다]")
	void getShowDetailInfoTest() {
		// given
		Place place = new Place(
			"블루스퀘어 신한카드홀",
			"1544-1591",
			"서울특별시 용산구 이태원로 294 블루스퀘어(한남동)",
			"http://www.bluesquare.kr/index.asp"
		);
		ShowPeriod showPeriod = new ShowPeriod(
			LocalDate.of(2023, 10, 10),
			LocalDate.of(2023, 10, 12)
		);
		ShowTime showTime = new ShowTime(
			150,
			15
		);
		Show show = new Show(
			"레미제라블",
			MUSICAL,
			showPeriod,
			showTime,
			"만 8세 이상",
			300,
			place
		);

		given(showRepository.findById(1L))
			.willReturn(Optional.of(show));

		// when
		ShowDetailsInfoResponse showDetailsInfoResponse = showService.getShowDetailInfo(1L);

		// then
		assertEquals(ShowMapper.toShowDetailInfoResponse(show), showDetailsInfoResponse);
	}

	@Test
	@DisplayName("[존재하지 않는 show_id 로 공연 조회 시 NotFoundException을 반환한다]")
	void getShowDetailInfo_WithNoShowId_Test() {

		// when then
		NotFoundException notFoundException = assertThrows(
			NotFoundException.class,
			() -> showService.getShowDetailInfo(1L)
		);
		assertEquals(ShowErrorCode.SHOW_NOT_FOUND.getMessage(), notFoundException.getMessage());
	}

}
