package dev.hooon.user;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.user.dto.request.UserJoinRequest;

@DisplayName("[UserController API 테스트]")
class UserApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[회원가입 API를 호출하면 회원이 등록되고 회원 ID를 응답한다]")
	void joinUserTest() throws Exception {
		// given
		UserJoinRequest userJoinRequest = new UserJoinRequest(
			"test@example.com", "password123", "name123"
		);

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(userJoinRequest))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.userId").exists());

		assertThat(userRepository.findByEmail("test@example.com")).isPresent();
	}

}
