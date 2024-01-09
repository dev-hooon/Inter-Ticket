package dev.hooon.waitingbooking;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.user.application.UserService;
import dev.hooon.user.domain.entity.User;
import dev.hooon.waitingbooking.domain.entity.WaitingBooking;
import dev.hooon.waitingbooking.domain.repository.WaitingBookingRepository;
import dev.hooon.waitingbooking.dto.request.WaitingRegisterRequest;

@DisplayName("[WaitingBookingApiController API 테스트]")
class WaitingBookingApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WaitingBookingRepository waitingBookingRepository;
	// TODO User 저장에 대한 로직을 다른 작업에서 병행중이어서 일단 모킹으로 대체하구 추후에 수정
	@MockBean
	private UserService userService;

	@Test
	@DisplayName("[예매대기 등록 API 를 호출하면 예매대기가 등록되고 예매대기 ID 를 응답한다]")
	void registerWaitingBookingTest() throws Exception {
		//given
		// TODO 추후에 User 생성 기능이 구현되면 수정 예정
		User user = new User();
		ReflectionTestUtils.setField(user, "id", 1L);
		given(userService.getUserById(1L)).willReturn(user);

		WaitingRegisterRequest waitingRegisterRequest = new WaitingRegisterRequest(2, List.of(1L, 2L, 3L, 4L));

		//when
		ResultActions actions = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/waiting_bookings")
				.queryParam("userId", "1") // TODO 추후에 인증정보 ArgumentResolver 가 구현되면 수정 예정
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
