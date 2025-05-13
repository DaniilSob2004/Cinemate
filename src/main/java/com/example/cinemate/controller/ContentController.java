package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentRandomRequestDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
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

    @GetMapping(value = Endpoint.BY_COUNT)
    public ResponseEntity<?> getRandom(@Valid ContentRandomRequestDto contentRandomRequestDto) {
        ErrorResponseDto errorResponseDto;
        try {
            return ResponseEntity.ok(contentCrudService.getRandom(contentRandomRequestDto));
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
