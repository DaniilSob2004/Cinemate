package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.genre.*;
import com.example.cinemate.dto.genre.file.GenreFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.mapper.genre.GenreFileMapper;
import com.example.cinemate.service.business.genre.GenreCrudService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.GENRES)
public class GenreController {

    private final GenreCrudService genreCrudService;
    private final GenreFileMapper genreFileMapper;
    private final CommonMapper commonMapper;

    public GenreController(GenreCrudService genreCrudService, GenreFileMapper genreFileMapper, CommonMapper commonMapper) {
        this.genreCrudService = genreCrudService;
        this.genreFileMapper = genreFileMapper;
        this.commonMapper = commonMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<GenreDto> genres = genreCrudService.getAll();
        Logger.info("Successfully retrieved " + genres.size() + " genres");
        return ResponseEntity.ok(genres);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata") String metadataStr,
            @RequestPart(value = "image") MultipartFile image
    ) throws IOException {

        // получение dto и проверка валидности
        var genreDto = commonMapper.toDtoAndValidation(metadataStr, GenreDto.class);

        var genreFilesDto = new GenreFilesDto(image);
        var genreFilesBufferDto = genreFileMapper.toGenreFilesBufferDto(genreFilesDto);
        Logger.info("-------- Add genre for admin (" + genreDto + ") " + " (" + genreFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем картинку
        var savedGenre = genreCrudService.add(genreDto);
        genreCrudService.uploadFilesAndUpdate(savedGenre, genreFilesBufferDto);

        return ResponseEntity.ok("Genre added successfully. Image are loading...");
    }

    @PostMapping(value = Endpoint.BY_RECOMMENDATIONS_TEST)
    public ResponseEntity<?> addGenresTest(@RequestBody GenreRecTestDto genreRecTestDto, HttpServletRequest request) {
        Logger.info("-------- Add genre for user test (" + genreRecTestDto + ") --------");
        genreCrudService.addGenresTest(genreRecTestDto, request);
        return ResponseEntity.ok("Genres for user test added successfully");
    }

    @GetMapping(value = "get-genre-test")
    public ResponseEntity<?> getGenreTest(HttpServletRequest request) {
        return ResponseEntity.ok(genreCrudService.getGenreTest(request));
    }
}
