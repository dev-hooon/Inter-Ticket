package dev.hooon.booking;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.support.ApiTestSupport;

@DisplayName("[BookingApiController API 테스트]")
@Sql("/sql/user_bookings_find.sql")
class BookingApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("사용자는 조회 기간에 따른 예매 정보를 조회할 수 있다 - 1")
	@Test
	void getBookings_test_1() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/users/booking?duration=3600&page=0&size=2")
				.queryParam("userId", "1")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.bookingList.length()").value(2),

			jsonPath("$.bookingList[0].bookingId").isNumber(),
			jsonPath("$.bookingList[0].bookingDate").isString(),
			jsonPath("$.bookingList[0].showInfo.showName").isString(),
			jsonPath("$.bookingList[0].showInfo.showDate").isString(),
			jsonPath("$.bookingList[0].showInfo.showRound").isNumber(),
			jsonPath("$.bookingList[0].showInfo.showRoundStartTime").isString(),
			jsonPath("$.bookingList[0].ticketNumber").isNumber(),
			jsonPath("$.bookingList[0].currentState").isString(),

			jsonPath("$.bookingList[1].bookingId").isNumber(),
			jsonPath("$.bookingList[1].bookingDate").isString(),
			jsonPath("$.bookingList[1].showInfo.showName").isString(),
			jsonPath("$.bookingList[1].showInfo.showDate").isString(),
			jsonPath("$.bookingList[1].showInfo.showRound").isNumber(),
			jsonPath("$.bookingList[1].showInfo.showRoundStartTime").isString(),
			jsonPath("$.bookingList[1].ticketNumber").isNumber(),
			jsonPath("$.bookingList[1].currentState").isString()
		);
	}

	@DisplayName("사용자는 조회 기간에 따른 예매 정보를 조회할 수 있다 - 2")
	@Test
	void getBookings_test_2() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/users/booking?duration=30&page=0&size=2")
				.queryParam("userId", "1")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.bookingList.length()").value(1),

			jsonPath("$.bookingList[0].bookingId").isNumber(),
			jsonPath("$.bookingList[0].bookingDate").isString(),
			jsonPath("$.bookingList[0].showInfo.showName").isString(),
			jsonPath("$.bookingList[0].showInfo.showDate").isString(),
			jsonPath("$.bookingList[0].showInfo.showRound").isNumber(),
			jsonPath("$.bookingList[0].showInfo.showRoundStartTime").isString(),
			jsonPath("$.bookingList[0].ticketNumber").isNumber(),
			jsonPath("$.bookingList[0].currentState").isString()

		);
	}

	@DisplayName("사용자는 조회 기간에 따른 예매 정보를 조회할 수 있다 - 3")
	@Test
	void getBookings_test_3() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.get("/api/users/booking?duration=30&page=0&size=2")
				.queryParam("userId", "2")
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.bookingList.length()").value(0)

		);
	}
}
