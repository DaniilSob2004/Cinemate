package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.contentviewhistory.ContentHistoryParamsDto;
import com.example.cinemate.dto.contentviewhistory.ContentViewAddDto;
import com.example.cinemate.service.business.contentviewhistory.ContentViewHistoryCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENT_VIEWS)
@SecurityRequirement(name = "JWT")
@Tag(name = "ContentViewHistory", description = "Operations related to managing user contentViewHistories")
public class ContentViewHistoryController {

    private final ContentViewHistoryCrudService contentViewHistoryCrudService;

    public ContentViewHistoryController(ContentViewHistoryCrudService contentViewHistoryCrudService) {
        this.contentViewHistoryCrudService = contentViewHistoryCrudService;
    }

    @GetMapping(value = Endpoint.ME)
    @Operation(summary = "Get contentViewHistories", description = "Get contents and return PageResponse(ContentViewHistoryDto)")
    public ResponseEntity<?> getByUserId(@Valid @ModelAttribute ContentHistoryParamsDto contentHistoryParamsDto, HttpServletRequest request) {
        Logger.info("-------- Get ContentViewHistory (" + contentHistoryParamsDto + ") --------");
        return ResponseEntity.ok(contentViewHistoryCrudService.getByUserId(contentHistoryParamsDto, request));
    }

    @PostMapping
    @Operation(summary = "Add content view", description = "Add a new content view for user")
    public ResponseEntity<?> add(@Valid @RequestBody ContentViewAddDto contentViewAddDto, HttpServletRequest request) {
        Logger.info("-------- Add contentView for user (" + contentViewAddDto + ") --------");
        contentViewHistoryCrudService.add(contentViewAddDto, request);
        return ResponseEntity.ok("ContentView added successfully...");
    }
}
