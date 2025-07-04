package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.*;
import com.example.cinemate.service.business.content.ContentCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENTS)
@Tag(name = "Content", description = "Operations related to managing contents")
public class ContentController {

    private final ContentCrudService contentCrudService;

    public ContentController(ContentCrudService contentCrudService) {
        this.contentCrudService = contentCrudService;
    }

    @GetMapping(value = Endpoint.BY_RECOMMENDATIONS)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get contents by rec", description = "Get contents by user rec and return PageResponse(ContentDto)")
    public ResponseEntity<?> getByRecommendations(
            @Valid @ModelAttribute ContentRecSearchParamsDto contentRecSearchParamsDto,
            HttpServletRequest request
    ) {
        Logger.info("-------- Get rec content (" + contentRecSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByRecommend(contentRecSearchParamsDto, request));
    }

    @GetMapping(value = Endpoint.RANDOM)
    @Operation(summary = "Get contents by random", description = "Get contents by random and return List(ContentRandomRequestDto)")
    public ResponseEntity<?> getRandom(@Valid @ModelAttribute ContentRandomRequestDto contentRandomRequestDto) {
        Logger.info("-------- Get random content (" + contentRandomRequestDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getRandom(contentRandomRequestDto));
    }

    @GetMapping(value = Endpoint.BY_GENRE)
    @Operation(summary = "Get contents by content genre", description = "Get contents by content genre and return PageResponse(ContentDto)")
    public ResponseEntity<?> getByGenre(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get content by genre (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByGenre(contentSearchParamsDto));
    }

    @GetMapping(value = Endpoint.BY_CONTENT_TYPE)
    @Operation(summary = "Get contents by content type", description = "Get contents by content type and return PageResponse(ContentDto)")
    public ResponseEntity<?> getByType(@Valid @ModelAttribute ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get content by type (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentCrudService.getByType(contentSearchParamsDto));
    }
}
