package com.example.cinemate;

import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.service.db.CinemateInitializer;
import org.tinylog.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStarter {

    private final CinemateInitializer cinemateInitializer;
    private final AmazonS3Service amazonS3Service;

    public AppStarter(CinemateInitializer cinemateInitializer, AmazonS3Service amazonS3Service) {
        this.cinemateInitializer = cinemateInitializer;
        this.amazonS3Service = amazonS3Service;
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Logger.info("ApplicationRunner has started!");
            cinemateInitializer.autoBaseInitialize();

            // -------------------------------

            /*var postersNames = FileWorkUtil.getFileNames(
                    "src/main/resources/data/posters",
                    "posters"
            );

            "posters/Captain America_ Brave New World.jpg"

            var trailersNames = FileWorkUtil.getFileNames(
                    "src/main/resources/data/trailers",
                    "trailers"
            );
            amazonS3Service.uploadFiles(trailersNames);*/

            /*amazonS3Service.downloadFile(
                    "posters/Captain America_ Brave New World.jpg",
                    "src/main/resources/FROM_S3_POSTER2.jpg"
            );*/

            //String s = amazonS3Service.getCloudFrontUrl("trailers/Captain America_ Brave New World.mp4");
            //Logger.info("URL: " + s);
        };
    }
}
