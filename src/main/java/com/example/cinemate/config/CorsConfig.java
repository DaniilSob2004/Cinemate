package com.example.cinemate.config;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")  // разрешить CORS для всех URL на сервере
                        .allowedOriginPatterns("http://localhost:*")  // разрешить этот сайт
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // разрешенные методы
                        .allowedHeaders("Authorization", "Content-Type")  // разрешенные заголовки
                        .allowCredentials(true);  // разрешаем JWT/cookie
            }
        };
    }
}
