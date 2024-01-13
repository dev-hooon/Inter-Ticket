package dev.hooon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@Configuration
@EnableSchedulerLock(defaultLockAtLeastFor = "10s", defaultLockAtMostFor = "30s")
public class ShedLockConfig {

	@Bean
	public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
		String lockEnv = System.getProperty("spring.profiles.active");
		return new RedisLockProvider(redisConnectionFactory, lockEnv);
	}
}
