package dev.hooon.auth;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import dev.hooon.auth.application.AuthService;
import dev.hooon.auth.dto.TokenReIssueRequest;
import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.common.support.ApiTestSupport;
import dev.hooon.user.application.UserService;
import dev.hooon.user.dto.request.UserJoinRequest;

@DisplayName("[AuthApiController API 테스트]")
class AuthApiControllerTest extends ApiTestSupport {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthService authService;

	@BeforeEach
	void setUp() {
		UserJoinRequest userJoinRequest = new UserJoinRequest("user@example.com", "password123", "name123");
		userService.join(userJoinRequest);
	}

	@Test
	@DisplayName("[로그인 API를 호출하면 토큰이 응답된다]")
	void loginTest() throws Exception {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "password123");

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(toJson(authRequest))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(jsonPath("$.refreshToken").exists());
	}

	@Test
	@DisplayName("[토큰 재발급 API를 호출하면 새로운 엑세스 토큰이 응답된다]")
	void reIssueAccessTokenTest() throws Exception {
		// given
		AuthRequest authRequest = new AuthRequest("user@example.com", "password123");
		AuthResponse authResponse = authService.login(authRequest);
		String refreshToken = authResponse.refreshToken();
		TokenReIssueRequest tokenReIssueRequest = new TokenReIssueRequest(refreshToken);

		// when
		ResultActions actions = mockMvc.perform(
			post("/api/auth/token")
				.contentType(APPLICATION_JSON)
				.content(toJson(tokenReIssueRequest))
		);

		// then
		actions.andExpect(status().isOk())
			.andExpect(content().string(not(emptyOrNullString())));
	}
}
