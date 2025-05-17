package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentByGenreRequestDto;
import com.example.cinemate.dto.content.ContentRandomRequestDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.service.business.content.ContentCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENTS)
public class ContentController {

    private final ContentCrudService contentCrudService;

    public ContentController(ContentCrudService contentCrudService) {
        this.contentCrudService = contentCrudService;
    }

    @GetMapping(value = Endpoint.RANDOM)
    public ResponseEntity<?> getRandom(@Valid @ModelAttribute ContentRandomRequestDto contentRandomRequestDto) {
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info(contentRandomRequestDto);
            return ResponseEntity.ok(contentCrudService.getRandom(contentRandomRequestDto));
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @GetMapping(value = Endpoint.BY_GENRE)
    public ResponseEntity<?> getByGenre(@Valid @ModelAttribute ContentByGenreRequestDto contentByGenreRequestDto) {
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info(contentByGenreRequestDto);
            return ResponseEntity.ok(contentCrudService.getByGenre(contentByGenreRequestDto));
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
