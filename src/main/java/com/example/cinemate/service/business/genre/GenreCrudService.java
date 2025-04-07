package com.example.cinemate.service.business.genre;

import com.example.cinemate.dto.genre.GenreDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.mapper.GenreMapper;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreCrudService {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreCrudService(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    public List<GenreDto> getAll() {
        return genreService.findAll().stream()
                .map(genreMapper::toGenreDto)
                .toList();
    }

    public void add(final GenreDto genreDto) {
        genreDto.setName(genreDto.getName().toLowerCase());

        genreService.findByName(genreDto.getName())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Genre '" + genreDto.getName() + "' already exists");
                });

        genreService.save(genreMapper.toGenre(genreDto));
    }
}
