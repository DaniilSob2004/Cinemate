package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.dto.genre.*;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.service.business.genre.GenreCrudService;
import org.springframework.http.HttpStatus;
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
        ErrorResponseDto errorResponseDto;
        try {
            genreCrudService.add(genreDto);
            return ResponseEntity.ok("Genre added successfully");
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @PostMapping(value = Endpoint.BY_RECOMMENDATIONS_TEST)
    public ResponseEntity<?> addGenresTest(@RequestBody GenreRecTestDto genreRecTestDto, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info(genreRecTestDto);
            genreCrudService.addGenresTest(genreRecTestDto, request);
            return ResponseEntity.ok("Genres for user test added successfully");
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @GetMapping(value = "get-genre-test")
    public ResponseEntity<?> getGenreTest(HttpServletRequest request) {
        return ResponseEntity.ok(genreCrudService.getGenreTest(request));
    }
}
