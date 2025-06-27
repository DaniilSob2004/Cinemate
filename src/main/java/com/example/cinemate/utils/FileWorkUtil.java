package com.example.cinemate.utils;

import com.example.cinemate.dto.common.TempContentFile;
import lombok.experimental.UtilityClass;
import org.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class FileWorkUtil {

    public static Optional<TempContentFile> downloadImageAsTempFile(final String imageUrl) {
        int statusOk = 200;
        try {
            URL url = new URL(imageUrl);
            var connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.connect();

            int status = connection.getResponseCode();
            if (status != statusOk) {
                return Optional.empty();
            }

            try (InputStream in = connection.getInputStream();
                 var out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                return Optional.of(new TempContentFile(
                        out.toByteArray(),
                        "image",
                        connection.getContentType()
                ));
            }
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }

        return Optional.empty();
    }

    public static List<String> getFileNames(final String dir, final String parentPath) {
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
