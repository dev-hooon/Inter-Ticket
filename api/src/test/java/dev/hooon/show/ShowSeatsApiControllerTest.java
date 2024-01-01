package dev.hooon.show;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.support.ApiTestSupport;

@DisplayName("[ShowSeatsApiController API 테스트]")
@Sql("/sql/show_seats_dummy.sql")
class ShowSeatsApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("[공연 아이디, 날짜, 회차]를 통해 API 를 호출하면 해당 공연의 세부 정보를 조회할 수 있다")
	@Test
	void getShowSeatsInfoTest() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/shows/1/seats?date=2024-01-01&round=2")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.seatsInfo").isArray(),
			jsonPath("$.seatsInfo[0].grade").isString(),
			jsonPath("$.seatsInfo[0].leftSeats").isNumber(),
			jsonPath("$.seatsInfo[0].price").isNumber(),
			jsonPath("$.seatsInfo[0].seats[0].id").isNumber(),
			jsonPath("$.seatsInfo[0].seats[0].date").isString(),
			jsonPath("$.seatsInfo[0].seats[0].isBookingAvailable").isString(),
			jsonPath("$.seatsInfo[0].seats[0].seat").isBoolean(),
			jsonPath("$.seatsInfo[0].seats[0].positionInfo_sector").isString(),
			jsonPath("$.seatsInfo[0].seats[0].positionInfo_row").isString(),
			jsonPath("$.seatsInfo[0].seats[0].positionInfo_col").isNumber(),

			jsonPath("$.seatsInfo").isArray(),
			jsonPath("$.seatsInfo[1].grade").isString(),
			jsonPath("$.seatsInfo[1].leftSeats").isNumber(),
			jsonPath("$.seatsInfo[1].price").isNumber(),
			jsonPath("$.seatsInfo[1].seats[0].id").isNumber(),
			jsonPath("$.seatsInfo[1].seats[0].date").isString(),
			jsonPath("$.seatsInfo[1].seats[0].isBookingAvailable").isString(),
			jsonPath("$.seatsInfo[1].seats[0].seat").isBoolean(),
			jsonPath("$.seatsInfo[1].seats[0].positionInfo_sector").isString(),
			jsonPath("$.seatsInfo[1].seats[0].positionInfo_row").isString(),
			jsonPath("$.seatsInfo[1].seats[0].positionInfo_col").isNumber(),

			jsonPath("$.seatsInfo").isArray(),
			jsonPath("$.seatsInfo[2].grade").isString(),
			jsonPath("$.seatsInfo[2].leftSeats").isNumber(),
			jsonPath("$.seatsInfo[2].price").isNumber(),
			jsonPath("$.seatsInfo[2].seats[0].id").isNumber(),
			jsonPath("$.seatsInfo[2].seats[0].date").isString(),
			jsonPath("$.seatsInfo[2].seats[0].isBookingAvailable").isString(),
			jsonPath("$.seatsInfo[2].seats[0].seat").isBoolean(),
			jsonPath("$.seatsInfo[2].seats[0].positionInfo_sector").isString(),
			jsonPath("$.seatsInfo[2].seats[0].positionInfo_row").isString(),
			jsonPath("$.seatsInfo[2].seats[0].positionInfo_col").isNumber()
		);
	}

}
