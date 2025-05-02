package com.example.cinemate;

import com.example.cinemate.service.db.CinemateInitializer;
import org.tinylog.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

// Названия контента
        /*File folder = new File("src/main/resources/data/posters");
        File[] listOfFiles = folder.listFiles();
        Path resourceFile = Paths.get("src/main/resources/data/content/names.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(resourceFile)) {
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                        Logger.info(fileName);
                        writer.write(fileName);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Ошибка при записи в файл", e);
        }*/

// Постеры контента
        /*File folder = new File("src/main/resources/data/posters");
        File[] listOfFiles = folder.listFiles();
        Path resourceFile = Paths.get("src/main/resources/data/content/posters.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(resourceFile)) {
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String fileName = "src/main/resources/data/posters/" + file.getName();
                        Logger.info(fileName);
                        writer.write(fileName);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Ошибка при записи в файл", e);
        }*/

// Трейлеры контента
        /*File folder = new File("src/main/resources/data/trailers");
        File[] listOfFiles = folder.listFiles();
        Path resourceFile = Paths.get("src/main/resources/data/content/trailers.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(resourceFile)) {
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String fileName = "src/main/resources/data/posters/" + file.getName();
                        Logger.info(fileName);
                        writer.write(fileName);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Ошибка при записи в файл", e);
        }*/
