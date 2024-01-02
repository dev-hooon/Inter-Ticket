package dev.hooon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchedulerApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-core,application,application-scheduler");
		SpringApplication.run(SchedulerApplication.class, args);
	}
}
