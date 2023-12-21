package dev.hooon.show.application;

import static dev.hooon.show.dto.response.AbleBookingDateRoundResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hooon.show.domain.repository.SeatRepository;
import dev.hooon.show.dto.query.SeatDateRoundDto;
import dev.hooon.show.dto.response.AbleBookingDateRoundResponse;

@DisplayName("[ShowService 테스트]")
@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

	@InjectMocks
	private ShowService showService;
	@Mock
	private SeatRepository seatRepository;

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
}