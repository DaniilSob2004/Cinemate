package com.example.cinemate;

import org.tinylog.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStarter {

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Logger.info("ApplicationRunner has started!");
            System.out.println("-".repeat(20));

            /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encPassword = passwordEncoder.encode("123456");
            Logger.info("Encrypted password: " + encPassword);*/
        };
    }
}

/*
spring:
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
* */
