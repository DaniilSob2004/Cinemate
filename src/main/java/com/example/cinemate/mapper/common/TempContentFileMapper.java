package com.example.cinemate.mapper.common;

import com.example.cinemate.dto.common.TempContentFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class TempContentFileMapper {

    // преобразования MultipartFile в TempContentFile
    public TempContentFile toTempContentFile(final MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return new TempContentFile(file.getBytes(), file.getOriginalFilename(), file.getContentType());
    }
}
