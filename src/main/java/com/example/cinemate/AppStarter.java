package com.example.cinemate;

import com.example.cinemate.service.db.CinemateInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.tinylog.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStarter {

    @Autowired
    private CinemateInitializer cinemateInitializer;

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Logger.info("ApplicationRunner has started!");
            cinemateInitializer.autoBaseInitialize();
        };
    }
}

/*
spring:
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
* */
