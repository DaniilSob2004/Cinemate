package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.genre.*;
import com.example.cinemate.service.business.genre.GenreCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.GENRES)
public class GenreController {

    private final GenreCrudService genreCrudService;

    public GenreController(GenreCrudService genreCrudService) {
        this.genreCrudService = genreCrudService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<GenreDto> genres = genreCrudService.getAll();
        Logger.info("Successfully retrieved " + genres.size() + " genres");
        return ResponseEntity.ok(genres);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody GenreDto genreDto) {
        Logger.info("-------- Add genre (" + genreDto + ") --------");
        genreCrudService.add(genreDto);
        return ResponseEntity.ok("Genre added successfully");
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
