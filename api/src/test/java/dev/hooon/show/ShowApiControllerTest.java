package dev.hooon.show;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.fixture.SeatFixture;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.show.domain.entity.seat.Seat;
import dev.hooon.show.domain.repository.SeatRepository;

@DisplayName("[ShowApiController API 테스트]")
class ShowApiControllerTest extends ApiTestSupport {

	@Autowired
	private SeatRepository seatRepository;

	@Test
	@DisplayName("[공연 아이디를 통해 API 를 호출하면 해당 공연의 예매 가능한 날짜와 회차 정보를 응답한다]")
	void getAbleBookingDateRoundInfoTest() throws Exception {
		//given
		LocalTime time = LocalTime.of(12, 34, 56);
		LocalDate date = LocalDate.of(2023, 10, 10);
		List<Seat> seats = List.of(
			SeatFixture.getSeat(date, 1, time),
			SeatFixture.getSeat(date, 1, time),
			SeatFixture.getSeat(date.plusDays(1), 1, time.plusHours(1)),
			SeatFixture.getSeat(date.plusDays(1), 1, time.plusHours(1))
		);
		seatRepository.saveAll(seats);

		//when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/shows/1/available")
		);

		//then
		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.availableDates.size()").value(2),
			jsonPath("$.availableDates[0].showDate").value("2023-10-10"),
			jsonPath("$.availableDates[0].round").value(1),
			jsonPath("$.availableDates[0].startTime").value("12:34:56"),
			jsonPath("$.availableDates[1].showDate").value("2023-10-11"),
			jsonPath("$.availableDates[1].round").value(1),
			jsonPath("$.availableDates[1].startTime").value("13:34:56")
		);
	}
}