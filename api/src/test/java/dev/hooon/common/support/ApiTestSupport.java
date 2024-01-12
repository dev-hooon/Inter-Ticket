package dev.hooon.common.support;

import static org.springframework.http.MediaType.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hooon.auth.dto.request.AuthRequest;
import dev.hooon.auth.dto.response.AuthResponse;
import dev.hooon.user.domain.repository.UserRepository;
import dev.hooon.user.dto.request.UserJoinRequest;
import jakarta.annotation.PostConstruct;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestSupport extends TestContainerSupport {

	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected UserRepository userRepository;

	protected static String accessToken;
	protected static String refreshToken;

	protected String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	@PostConstruct
	public void setUpUser() throws Exception {
		// 캐싱해서 단 한번만 호출
		if (accessToken != null && refreshToken != null) {
			return;
		}

		System.out.println("hello world");
		UserJoinRequest joinRequest = new UserJoinRequest("hello123@email.com", "password123@", "userA");
		mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/users")
				.contentType(APPLICATION_JSON)
				.content(toJson(joinRequest))
		);

		AuthRequest authRequest = new AuthRequest(joinRequest.email(), joinRequest.password());
		MvcResult loginResult = mockMvc.perform(
			MockMvcRequestBuilders
				.post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(toJson(authRequest))
		).andReturn();

		String stringLoginResponse = loginResult.getResponse().getContentAsString();
		AuthResponse authResponse = objectMapper.readValue(stringLoginResponse, AuthResponse.class);

		accessToken = authResponse.accessToken();
		refreshToken = authResponse.refreshToken();
	}
}
