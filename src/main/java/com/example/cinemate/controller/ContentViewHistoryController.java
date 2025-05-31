package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.contentviewhistory.ContentHistoryParamsDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.service.business.contentviewhistory.ContentViewHistoryCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENT_VIEWS)
public class ContentViewHistoryController {

    private final ContentViewHistoryCrudService contentViewHistoryCrudService;

    public ContentViewHistoryController(ContentViewHistoryCrudService contentViewHistoryCrudService) {
        this.contentViewHistoryCrudService = contentViewHistoryCrudService;
    }

    @GetMapping(value = Endpoint.ME)
    public ResponseEntity<?> getByUserId(@Valid @ModelAttribute ContentHistoryParamsDto contentHistoryParamsDto, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            return ResponseEntity.ok(contentViewHistoryCrudService.getByUserId(contentHistoryParamsDto, request));
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
