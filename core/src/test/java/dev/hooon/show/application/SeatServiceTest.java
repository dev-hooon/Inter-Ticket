package dev.hooon.show.application;

import static dev.hooon.show.dto.response.ShowSeatResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.fixture.TestFixture;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.dto.query.seats.SeatsDetailDto;
import dev.hooon.show.dto.request.BookedSeatQueryRequest;
import dev.hooon.show.dto.response.ShowSeatResponse;

@DisplayName("[SeatService 테스트]")
@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

	@InjectMocks
	private SeatService seatService;
	@Mock
	private SeatRepository seatRepository;

	@Test
	@DisplayName("[취소된 좌석들의 id 를 조회한다]")
	void getCanceledSeatIds_test() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(1L),
			SeatFixture.getSeat(2L),
			SeatFixture.getSeat(3L)
		);
		given(seatRepository.findByStatusIsCanceled())
			.willReturn(seats);

		//when
		Set<Long> result = seatService.getCanceledSeatIds();

		//then
		assertThat(result).hasSameSizeAs(seats);
		seats.forEach(seat -> assertThat(result).contains(seat.getId()));
	}

	@Test
	@DisplayName("[예약된 좌석들의 정보를 조회한다]")
	void getBookedSeatsInfo_test() {
		//given
		List<Seat> seats = List.of(
			SeatFixture.getSeat(1L),
			SeatFixture.getSeat(2L),
			SeatFixture.getSeat(3L)
		);
		List<SeatsDetailDto> seatsDetails = TestFixture.seatListToSeatsDetailDto(seats);

		given(seatRepository.findBookedSeatsByShowIdAndDateAndRound(1L, seats.get(0).getShowDate(), 1))
			.willReturn(seatsDetails);

		BookedSeatQueryRequest request = new BookedSeatQueryRequest(1L, seats.get(0).getShowDate(), 1);

		//when
		ShowSeatResponse result = seatService.getBookedSeatsInfo(request);

		//then
		List<SeatsDetailInfo> seatsDetailInfos = result.seatsDetailInfos();

		for (int i = 0; i < seatsDetailInfos.size(); i++) {
			SeatsDetailInfo actual = seatsDetailInfos.get(i);
			SeatsDetailDto expected = seatsDetails.get(i);

			assertAll(
				() -> assertThat(actual.id()).isEqualTo(expected.id()),
				() -> assertThat(actual.isSeat()).isEqualTo(expected.isSeat()),
				() -> assertThat(actual.col()).isEqualTo(expected.col()),
				() -> assertThat(actual.row()).isEqualTo(expected.row()),
				() -> assertThat(actual.date()).isEqualTo(expected.date()),
				() -> assertThat(actual.price()).isEqualTo(expected.price()),
				() -> assertThat(actual.sector()).isEqualTo(expected.sector()),
				() -> assertThat(actual.status()).isEqualTo(expected.isBookingAvailable())
			);
		}
	}
}