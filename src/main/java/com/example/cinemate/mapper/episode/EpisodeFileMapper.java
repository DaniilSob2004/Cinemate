package com.example.cinemate.mapper.episode;

import com.example.cinemate.dto.episode.file.EpisodeFilesBufferDto;
import com.example.cinemate.dto.episode.file.EpisodeFilesDto;
import com.example.cinemate.mapper.common.TempContentFileMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EpisodeFileMapper {

    private final TempContentFileMapper tempContentFileMapper;

    public EpisodeFileMapper(TempContentFileMapper tempContentFileMapper) {
        this.tempContentFileMapper = tempContentFileMapper;
    }

    // преобразования MultipartFile в EpisodeFilesBufferDto (TempContentFile)
    public EpisodeFilesBufferDto toEpisodeFilesBufferDto(final EpisodeFilesDto episodeFilesDto) {
        try {
            return new EpisodeFilesBufferDto(
                    tempContentFileMapper.toTempContentFile(episodeFilesDto.getTrailer()),
                    tempContentFileMapper.toTempContentFile(episodeFilesDto.getVideo())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file: " + e.getMessage());
        }
    }
}
