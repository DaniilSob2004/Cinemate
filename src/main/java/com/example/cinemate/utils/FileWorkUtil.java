package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FileWorkUtil {

    public List<String> getFileNames(final String dir, final String parentPath) {
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            return List.of();
        }

        List<String> postersNames = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String filename = Paths.get(parentPath, file.getName()).toString().replace("\\", "/");
                postersNames.add(filename);
            }
        }

        return postersNames;
    }
}
