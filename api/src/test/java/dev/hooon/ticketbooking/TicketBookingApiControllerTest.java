package dev.hooon.ticketbooking;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.booking.dto.request.TicketBookingRequest;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;

@DisplayName("[TicketBookingApiController API 테스트]")
@Sql("/sql/booking_dummy.sql")
class TicketBookingApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@DisplayName("사용자는 티켓을 예매할 수 있다")
	@Test
	void bookingTicketTest() throws Exception {

		// given
		TicketBookingRequest ticketBookingRequest = new TicketBookingRequest(List.of(1L, 2L, 3L));
		User user = new User();
		ReflectionTestUtils.setField(user, "id", 1L);
		given(userService.getUserById(1L)).willReturn(user);

		// when
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/bookings")
				.queryParam("userId", "1") // TODO: 인증 구현되면 수정할 것
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(toJson(ticketBookingRequest))
		);

		// then
		resultActions.andExpectAll(
			status().isOk(),

			jsonPath("$.bookingId").isNumber(),
			jsonPath("$.bookingTickets").isArray(),

			jsonPath("$.bookingTickets[0].ticketId").isNumber(),
			jsonPath("$.bookingTickets[0].showName").isString(),
			jsonPath("$.bookingTickets[0].seatGrade").isString(),
			jsonPath("$.bookingTickets[0].seatPositionInfo").isString(),
			jsonPath("$.bookingTickets[0].seatStatus").value("BOOKED"),
			jsonPath("$.bookingTickets[0].showDate").isString(),
			jsonPath("$.bookingTickets[0].showTime").isString(),
			jsonPath("$.bookingTickets[0].showRound").isNumber(),

			jsonPath("$.bookingTickets[1].ticketId").isNumber(),
			jsonPath("$.bookingTickets[1].showName").isString(),
			jsonPath("$.bookingTickets[1].seatGrade").isString(),
			jsonPath("$.bookingTickets[1].seatPositionInfo").isString(),
			jsonPath("$.bookingTickets[1].seatStatus").value("BOOKED"),
			jsonPath("$.bookingTickets[1].showDate").isString(),
			jsonPath("$.bookingTickets[1].showTime").isString(),
			jsonPath("$.bookingTickets[1].showRound").isNumber(),

			jsonPath("$.bookingTickets[2].ticketId").isNumber(),
			jsonPath("$.bookingTickets[2].showName").isString(),
			jsonPath("$.bookingTickets[2].seatGrade").isString(),
			jsonPath("$.bookingTickets[2].seatPositionInfo").isString(),
			jsonPath("$.bookingTickets[2].seatStatus").value("BOOKED"),
			jsonPath("$.bookingTickets[2].showDate").isString(),
			jsonPath("$.bookingTickets[2].showTime").isString(),
			jsonPath("$.bookingTickets[2].showRound").isNumber()
		);
	}
}
