package com.example.cinemate.mapper.genre;

import com.example.cinemate.dto.genre.file.GenreFilesBufferDto;
import com.example.cinemate.dto.genre.file.GenreFilesDto;
import com.example.cinemate.mapper.common.TempContentFileMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GenreFileMapper {

    private final TempContentFileMapper tempContentFileMapper;

    public GenreFileMapper(TempContentFileMapper tempContentFileMapper) {
        this.tempContentFileMapper = tempContentFileMapper;
    }

    public GenreFilesBufferDto toGenreFilesBufferDto(final GenreFilesDto genreFilesDto) {
        try {
            return new GenreFilesBufferDto(
                    tempContentFileMapper.toTempContentFile(genreFilesDto.getImage())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file: " + e.getMessage());
        }
    }
}
