package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.dto.genre.GenreDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.service.business.genre.GenreCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

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
        try {
            List<GenreDto> genres = genreCrudService.getAll();
            Logger.info("Successfully retrieved " + genres.size() + " genres");
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody GenreDto genreDto) {
        ErrorResponseDto errorResponseDto;
        try {
            genreCrudService.add(genreDto);
            return ResponseEntity.ok("Genre added successfully");
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }
}
