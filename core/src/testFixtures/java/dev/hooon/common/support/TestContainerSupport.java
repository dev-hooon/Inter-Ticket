package dev.hooon.common.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class TestContainerSupport {

	protected TestContainerSupport() {
	}

	private static final String REDIS_IMAGE = "redis:latest";
	private static final String POSTGRES_IMAGE = "postgres:13-alpine";
	private static final int REDIS_PORT = 6379;

	private static final int POSTGRES_PORT = 5432;
	private static final GenericContainer<?> REDIS;

	private static final PostgreSQLContainer<?> POSTGRES;

	static {
		REDIS = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
			.withExposedPorts(REDIS_PORT)
			.withReuse(true);
		POSTGRES = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
			.withExposedPorts(POSTGRES_PORT)
			.withReuse(true);

		REDIS.start();
		POSTGRES.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry){
		registry.add("spring.data.redis.host", REDIS::getHost);
		registry.add("spring.data.redis.port", () -> String.valueOf(REDIS.getMappedPort(REDIS_PORT)));

		registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
		registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRES::getUsername);
		registry.add("spring.datasource.password", POSTGRES::getPassword);
	}
}
