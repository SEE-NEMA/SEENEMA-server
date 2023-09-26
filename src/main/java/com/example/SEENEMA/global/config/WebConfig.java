package com.example.SEENEMA.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://192.168.0.127:3000",
                        "http://223.194.131.127:3000",
                        "http://223.194.130.149:3000"
                        )
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
