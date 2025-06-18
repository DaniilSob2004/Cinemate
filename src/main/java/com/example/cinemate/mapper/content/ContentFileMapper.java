package com.example.cinemate.mapper.content;

import com.example.cinemate.dto.content.file.ContentFilesBufferDto;
import com.example.cinemate.dto.content.file.ContentFilesDto;
import com.example.cinemate.mapper.common.TempContentFileMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ContentFileMapper {

    private final TempContentFileMapper tempContentFileMapper;

    public ContentFileMapper(TempContentFileMapper tempContentFileMapper) {
        this.tempContentFileMapper = tempContentFileMapper;
    }

    // преобразования MultipartFile в ContentFilesBufferDto (TempContentFile)
    public ContentFilesBufferDto toContentFilesBufferDto(final ContentFilesDto contentFilesDto) {
        try {
            return new ContentFilesBufferDto(
                    tempContentFileMapper.toTempContentFile(contentFilesDto.getPoster()),
                    tempContentFileMapper.toTempContentFile(contentFilesDto.getTrailer()),
                    tempContentFileMapper.toTempContentFile(contentFilesDto.getVideo())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file: " + e.getMessage());
        }
    }
}
