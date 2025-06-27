package com.example.cinemate.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileWorkUtilTest {

    @Test
    void getFileNames_ShouldReturnFileNamesWithParentPath() throws IOException {
        File tempDir = Files.createTempDirectory("testDir").toFile();
        File file1 = new File(tempDir, "img1.jpg");
        File file2 = new File(tempDir, "img2.png");
        File subDir = new File(tempDir, "subfolder");

        assertTrue(file1.createNewFile());
        assertTrue(file2.createNewFile());
        assertTrue(subDir.mkdir());

        String parentPath = "/uploads/images";
        List<String> result = FileWorkUtil.getFileNames(tempDir.getAbsolutePath(), parentPath);

        assertEquals(2, result.size());
        assertTrue(result.contains("/uploads/images/img1.jpg"));
        assertTrue(result.contains("/uploads/images/img2.png"));

        file1.delete();
        file2.delete();
        subDir.delete();
        tempDir.delete();
    }

    @Test
    void getFileNames_ShouldReturnEmptyListIfDirectoryNotExists() {
        String fakeDir = "some/non/existing/path";
        List<String> result = FileWorkUtil.getFileNames(fakeDir, "/fake");
        assertTrue(result.isEmpty());
    }

    @Test
    void getFileNames_ShouldReturnEmptyListIfNoFiles() throws IOException {
        File tempDir = Files.createTempDirectory("emptyDir").toFile();

        List<String> result = FileWorkUtil.getFileNames(tempDir.getAbsolutePath(), "/empty");
        assertTrue(result.isEmpty());

        tempDir.delete();
    }
}