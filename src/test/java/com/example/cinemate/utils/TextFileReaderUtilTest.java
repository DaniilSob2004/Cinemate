package com.example.cinemate.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderUtilTest {

    @Test
    void readTextFile_ShouldReadLinesCorrectly() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of("line 1", "line 2"));

        List<String> lines = TextFileReaderUtil.ReadTextFile(tempFile.toString());

        assertEquals(2, lines.size());
        assertEquals("line 1", lines.get(0));
        assertEquals("line 2", lines.get(1));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void readTextFile_ShouldReturnEmptyListForEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".txt");
        List<String> lines = TextFileReaderUtil.ReadTextFile(tempFile.toString());

        assertTrue(lines.isEmpty());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void readTextFile_ShouldThrowRuntimeExceptionForMissingFile() {
        String nonExistentFile = "some_non_existing_file_123456789.txt";

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> TextFileReaderUtil.ReadTextFile(nonExistentFile)
        );

        assertInstanceOf(NoSuchFileException.class, exception.getCause());
    }
}