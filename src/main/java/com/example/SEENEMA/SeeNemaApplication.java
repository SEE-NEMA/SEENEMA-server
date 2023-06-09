package com.example.SEENEMA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@EnableJpaAuditing
@EnableSwagger2
@SpringBootApplication
public class SeeNemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeeNemaApplication.class, args);
	}

}
