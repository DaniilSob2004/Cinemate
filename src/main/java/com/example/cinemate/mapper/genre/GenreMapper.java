package com.example.cinemate.mapper.genre;

import com.example.cinemate.dto.genre.GenreDto;
import com.example.cinemate.model.db.Genre;
import com.example.cinemate.service.amazon.AmazonS3Service;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    private final AmazonS3Service amazonS3Service;

    public GenreMapper(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    public GenreDto toGenreDto(final Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName(),
                amazonS3Service.getCloudFrontUrl(genre.getImageUrl()),
                genre.getDescription(),
                genre.getTags()
        );
    }

    public Genre toGenre(final GenreDto genreDto) {
        return new Genre(
                genreDto.getId(),
                genreDto.getName(),
                genreDto.getImageUrl(),
                genreDto.getDescription(),
                genreDto.getTags()
        );
    }
}
