package com.example.cinemate.mapper.content;

import com.example.cinemate.dto.content.ContentFilesDto;
import com.example.cinemate.dto.content.files.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ContentFileMapper {

    // преобразования MultipartFile в TempContentFile
    public ContentFilesBufferDto toContentFilesBufferDto(final ContentFilesDto contentFilesDto) {
        try {
            return new ContentFilesBufferDto(
                    toTempContentFile(contentFilesDto.getPoster()),
                    toTempContentFile(contentFilesDto.getTrailer()),
                    toTempContentFile(contentFilesDto.getVideo())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file: " + e.getMessage());
        }
    }

    private TempContentFile toTempContentFile(final MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return new TempContentFile(file.getBytes(), file.getOriginalFilename(), file.getContentType());
    }
}
