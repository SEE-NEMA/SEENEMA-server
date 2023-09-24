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
                        "http://192.168.219.100:3000",
                        "https://seenema-client-9qzldldc1-seenema.vercel.app/"
                        )
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
