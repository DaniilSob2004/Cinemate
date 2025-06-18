package com.example.cinemate;

import com.example.cinemate.service.db.CinemateInitializer;
import org.tinylog.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStarter {

    private final CinemateInitializer cinemateInitializer;

    public AppStarter(CinemateInitializer cinemateInitializer) {
        this.cinemateInitializer = cinemateInitializer;
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Logger.info("ApplicationRunner has started!");
            cinemateInitializer.autoBaseInitialize();
        };
    }
}
