package dev.hooon.show.application;

import static dev.hooon.show.domain.entity.seat.SeatGrade.*;
import static dev.hooon.show.domain.entity.seat.SeatStatus.*;
import static dev.hooon.show.dto.SeatMapper.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.common.fixture.TestFixture;
import dev.hooon.show.domain.entity.Show;
import dev.hooon.show.domain.entity.place.Place;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.query.seats.SeatsInfoDto;
import dev.hooon.show.dto.response.seats.SeatsInfoResponse;
import dev.hooon.show.dto.response.seats.ShowSeatsResponse;

@DisplayName("[ShowSeatsService 테스트]")
@ExtendWith(MockitoExtension.class)
class ShowSeatsServiceTest {

	@InjectMocks
	private ShowSeatsService showSeatsService;

	@Mock
	private SeatRepository seatRepository;

	@Mock
	private ShowRepository showRepository;

	@DisplayName("[showId, date, round 에 해당하는 좌석 정보에 대해 조회할 수 있다]")
	@Test
	void findShowSeatsByShowIdAndDateAndRound_test() {
		// given
		Long showId = 1L;
		LocalDate date = LocalDate.of(2024, 1, 1);
		int round = 2;

		Place place = TestFixture.getPlace();
		Show show = TestFixture.getShow(place);

		List<SeatsInfoDto> seatsInfoDtoList = List.of(
			new SeatsInfoDto(VIP, 2L, 100000),
			new SeatsInfoDto(S, 2L, 70000),
			new SeatsInfoDto(A, 1L, 50000)
		);

		List<SeatsDetailDto> vipSeatsDetailDtoList = List.of(
			new SeatsDetailDto(1L, date, true, "1층", "A", 2, 100000, AVAILABLE),
			new SeatsDetailDto(2L, date, true, "1층", "A", 3, 100000, AVAILABLE)
		);
		List<SeatsDetailDto> sSeatsDetailDtoList = List.of(
			new SeatsDetailDto(1L, date, true, "2층", "A", 2, 70000, AVAILABLE),
			new SeatsDetailDto(2L, date, true, "2층", "A", 3, 70000, AVAILABLE)
		);
		List<SeatsDetailDto> aSeatsDetailDtoList = List.of(
			new SeatsDetailDto(1L, date, true, "3층", "A", 2, 50000, AVAILABLE)
		);

		List<SeatsInfoResponse> seatsInfoResponseList = seatsInfoDtoList.stream()
			.map(it -> new SeatsInfoResponse(
				it.grade(),
				it.leftSeats(),
				it.price()
			))
			.toList();

		seatsInfoResponseList.get(0).setSeats(getSeatsDetailResponses(vipSeatsDetailDtoList));
		seatsInfoResponseList.get(1).setSeats(getSeatsDetailResponses(sSeatsDetailDtoList));
		seatsInfoResponseList.get(2).setSeats(getSeatsDetailResponses(aSeatsDetailDtoList));

		given(showRepository.findById(showId)).willReturn(Optional.of(show));

		given(seatRepository.findSeatInfoByShowIdAndDateAndRound(showId, date, round))
			.willReturn(seatsInfoDtoList);
		given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, VIP))
			.willReturn(vipSeatsDetailDtoList);
		given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, S))
			.willReturn(sSeatsDetailDtoList);
		given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, A))
			.willReturn(aSeatsDetailDtoList);

		// when
		ShowSeatsResponse showSeatsResponse = showSeatsService.findShowSeatsByShowIdAndDateAndRound(
			showId,
			date,
			round
		);

		// then
		assertThat(showSeatsResponse.getSeatsInfo()).isEqualTo(seatsInfoResponseList);
	}
}
