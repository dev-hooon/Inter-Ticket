package dev.hooon.booking;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.user.domain.entity.User;

@DisplayName("[BookingCancelApiController API 테스트]")
@Sql("/sql/booking_cancel_dummy.sql")
class BookingCancelApiControllerTest extends ApiTestSupport {

	private long getBookingId(ResultActions resultActions) throws Exception {
		String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
		JSONObject jsonObject = new JSONObject(contentAsString);
		return Long.parseLong(jsonObject.getString("bookingId"));
	}

	private ResultActions doBooking() throws Exception {
		TicketBookingRequest ticketBookingRequest = new TicketBookingRequest(List.of(1L, 2L, 3L));
		return mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/bookings")
				.header(AUTHORIZATION, accessToken)
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(toJson(ticketBookingRequest))
		);
	}

	@DisplayName("사용자는 예매한 티켓을 취소할 수 있다.")
	@Test
	void cancelBookingTest() throws Exception {
		// given
		User user = new User();
		ReflectionTestUtils.setField(user, "id", 1L);

		// 예약 API 먼저 수행
		ResultActions bookingResultActions = doBooking();
		long bookingId = getBookingId(bookingResultActions);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/bookings/cancel/" + bookingId)
				.header(AUTHORIZATION, accessToken)
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.bookingStatus").isString(),
			jsonPath("$.canceledTickets").isArray(),

			jsonPath("$.canceledTickets[0].seatId").isNumber(),
			jsonPath("$.canceledTickets[0].seatStatus").isString(),

			jsonPath("$.canceledTickets[1].seatId").isNumber(),
			jsonPath("$.canceledTickets[1].seatStatus").isString(),

			jsonPath("$.canceledTickets[2].seatId").isNumber(),
			jsonPath("$.canceledTickets[2].seatStatus").isString()
		);
	}

}
