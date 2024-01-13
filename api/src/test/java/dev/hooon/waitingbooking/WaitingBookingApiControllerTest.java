package dev.hooon.waitingbooking;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;

@DisplayName("[WaitingBookingApiController API 테스트]")
class WaitingBookingApiControllerTest extends ApiTestSupport {
	
	@Autowired
	private WaitingBookingRepository waitingBookingRepository;

	@Test
	@DisplayName("[예매대기 등록 API 를 호출하면 예매대기가 등록되고 예매대기 ID 를 응답한다]")
	void registerWaitingBookingTest() throws Exception {
		WaitingRegisterRequest waitingRegisterRequest = new WaitingRegisterRequest(2, List.of(1L, 2L, 3L, 4L));

		//when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/waiting_bookings")
				.header(AUTHORIZATION, accessToken)
				.contentType(APPLICATION_JSON)
				.content(toJson(waitingRegisterRequest))
		);

		//then
		List<WaitingBooking> allWaitingBookings = waitingBookingRepository.findAll();
		assertThat(allWaitingBookings).isNotEmpty();

		actions.andExpectAll(
			status().isOk(),
			jsonPath("$.waitingBookingId").value(allWaitingBookings.get(0).getId())
		);
	}
}
