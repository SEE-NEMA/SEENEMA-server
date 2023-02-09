package com.example.SEENEMA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SeeNemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeeNemaApplication.class, args);
	}

}
