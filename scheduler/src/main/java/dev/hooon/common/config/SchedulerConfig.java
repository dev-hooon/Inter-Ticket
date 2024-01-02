package dev.hooon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@Configuration
@EnableScheduling
@EnableAsync
@EnableSchedulerLock(defaultLockAtLeastFor = "10s", defaultLockAtMostFor = "30s")
public class SchedulerConfig {

	@Bean
	public LockProvider lockProvider(RedisConnectionFactory redisConnectionFactory) {
		String lockEnv = System.getProperty("spring.profiles.active");
		return new RedisLockProvider(redisConnectionFactory, lockEnv);
	}

	@Bean("scheduler")
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		executor.setPoolSize(5);
		executor.setThreadNamePrefix("scheduler-thread-");
		executor.initialize();
		return executor;
	}
}
