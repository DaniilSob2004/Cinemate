package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class TextFileReader {
    public static List<String> ReadTextFile(final String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
