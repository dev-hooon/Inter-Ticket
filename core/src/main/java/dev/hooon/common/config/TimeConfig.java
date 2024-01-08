package dev.hooon.common.config;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

	@Bean("nowLocalDateTimeSupplier")
	public Supplier<LocalDateTime> nowLocalDateTimeSupplier() {
		return LocalDateTime::now;
	}
}
