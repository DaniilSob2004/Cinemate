package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentRandomRequestDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.business.content.ContentCrudService;
import com.example.cinemate.utils.SendResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENTS)
public class ContentController {

    private final ContentCrudService contentCrudService;
    private final SendResponseUtil sendResponseUtil;

    public ContentController(ContentCrudService contentCrudService, SendResponseUtil sendResponseUtil) {
        this.contentCrudService = contentCrudService;
        this.sendResponseUtil = sendResponseUtil;
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
    public ResponseEntity<?> getByGenre(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        return sendResponseUtil.handleContentSearch(contentSearchParamsDto, contentCrudService::getByGenre);
    }

    @GetMapping(value = Endpoint.BY_CONTENT_TYPE)
    public ResponseEntity<?> getByType(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        return sendResponseUtil.handleContentSearch(contentSearchParamsDto, contentCrudService::getByType);
    }
}
