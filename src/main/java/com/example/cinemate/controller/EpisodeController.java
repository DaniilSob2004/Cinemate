package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.service.business.episode.EpisodeCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.EPISODES)
@SecurityRequirement(name = "JWT")
@Tag(name = "Episode", description = "Operations related to managing content episodes")
public class EpisodeController {

    private final EpisodeCrudService episodeCrudService;

    public EpisodeController(EpisodeCrudService episodeCrudService) {
        this.episodeCrudService = episodeCrudService;
    }

    @GetMapping(value = Endpoint.BY_CONTENT_ID)
    @Operation(summary = "Get episode by content id", description = "Returns episode details (EpisodesWrapperDto) for the given content id")
    public ResponseEntity<?> getByContentId(@PathVariable @Parameter(description = "Episode id", example = "8") Integer contentId) {
        Logger.info("Get episode by content id: " + contentId);
        return ResponseEntity.ok(episodeCrudService.getByContentId(contentId));
    }
}
