package dev.hooon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-core,application,application-scheduler");
        SpringApplication.run(ApiApplication.class, args);
    }
}
