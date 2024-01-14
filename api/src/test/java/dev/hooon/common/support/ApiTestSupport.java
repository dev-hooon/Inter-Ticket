package dev.hooon.common.support;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestSupport extends TestContainerSupport {

	protected ObjectMapper objectMapper = new ObjectMapper();

	protected String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@AfterEach
	public void cleanRedis() {
		redisTemplate.delete(redisTemplate.keys("*"));
	}
}
