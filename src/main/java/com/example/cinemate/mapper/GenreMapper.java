package com.example.cinemate.mapper;

import com.example.cinemate.dto.genre.GenreDto;
import com.example.cinemate.model.db.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public GenreDto toGenreDto(final Genre genre) {
        return new GenreDto(
                genre.getId(),
                genre.getName(),
                genre.getImageUrl(),
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
