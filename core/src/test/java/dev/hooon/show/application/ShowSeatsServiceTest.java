package dev.hooon.show.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.domain.repository.ShowRepository;

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
		// // given
		// Long showId = 1L;
		// LocalDate date = LocalDate.of(2024, 1, 1);
		// int round = 2;
		//
		// Place place = TestFixture.getPlace();
		// Show show = TestFixture.getShow(place);
		//
		// List<SeatsInfoResponse> seatsInfoResponseList = List.of(
		// 	new SeatsInfoResponse(VIP, 2L, 100000),
		// 	new SeatsInfoResponse(S, 2L, 70000),
		// 	new SeatsInfoResponse(A, 1L, 50000)
		// );
		//
		// List<SeatsDetailResponse> vipSeatsDetailResponseList = List.of(
		// 	new SeatsDetailResponse(1L, date, true, "1층", "A", 2, 100000, AVAILABLE),
		// 	new SeatsDetailResponse(2L, date, true, "1층", "A", 3, 100000, AVAILABLE)
		// );
		// List<SeatsDetailResponse> sSeatsDetailResponseList = List.of(
		// 	new SeatsDetailResponse(1L, date, true, "2층", "A", 2, 70000, AVAILABLE),
		// 	new SeatsDetailResponse(2L, date, true, "2층", "A", 3, 70000, AVAILABLE)
		// );
		// List<SeatsDetailResponse> aSeatsDetailResponseList = List.of(
		// 	new SeatsDetailResponse(1L, date, true, "3층", "A", 2, 50000, AVAILABLE)
		// );
		//
		// List<SeatsInfoDto> expected = new ArrayList<>(seatsInfoResponseList);
		// expected.get(0).setSeats(vipSeatsDetailResponseList);
		// expected.get(1).setSeats(sSeatsDetailResponseList);
		// expected.get(2).setSeats(aSeatsDetailResponseList);
		//
		// given(showRepository.findById(showId)).willReturn(Optional.of(show));
		//
		// given(seatRepository.findSeatInfoByShowIdAndDateAndRound(showId, date, round))
		// 	.willReturn(seatsInfoResponseList);
		// given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, VIP))
		// 	.willReturn(vipSeatsDetailResponseList);
		// given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, S))
		// 	.willReturn(sSeatsDetailResponseList);
		// given(seatRepository.findSeatsByShowIdAndDateAndRoundAndGrade(showId, date, round, A))
		// 	.willReturn(aSeatsDetailResponseList);
		//
		// // when
		// ShowSeatsResponse showSeatsResponse = showSeatsService.findShowSeatsByShowIdAndDateAndRound(
		// 	showId, date, round);
		//
		// // then
		// assertThat(showSeatsResponse.getSeatsInfo()).containsExactlyInAnyOrderElementsOf(expected);
	}
}
