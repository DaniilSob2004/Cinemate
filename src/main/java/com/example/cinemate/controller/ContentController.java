package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.*;
import com.example.cinemate.service.business.content.ContentCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENTS)
public class ContentController {

    private final ContentCrudService contentCrudService;

    public ContentController(ContentCrudService contentCrudService) {
        this.contentCrudService = contentCrudService;
    }

    @GetMapping(value = Endpoint.BY_RECOMMENDATIONS)
    public ResponseEntity<?> getByRecommendations(@Valid @ModelAttribute ContentRecSearchParamsDto contentRecSearchParamsDto, HttpServletRequest request) {
        Logger.info("-------- Get rec content (" + contentRecSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByRecommend(contentRecSearchParamsDto, request));
    }

    @GetMapping(value = Endpoint.RANDOM)
    public ResponseEntity<?> getRandom(@Valid @ModelAttribute ContentRandomRequestDto contentRandomRequestDto) {
        Logger.info("-------- Get random content (" + contentRandomRequestDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getRandom(contentRandomRequestDto));
    }

    @GetMapping(value = Endpoint.BY_GENRE)
    public ResponseEntity<?> getByGenre(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get content by genre (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByGenre(contentSearchParamsDto));
    }

    @GetMapping(value = Endpoint.BY_CONTENT_TYPE)
    public ResponseEntity<?> getByType(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get content by type (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByType(contentSearchParamsDto));
    }
}
