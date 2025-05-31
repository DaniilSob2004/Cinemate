package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.service.business.episode.EpisodeCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.EPISODES)
public class EpisodeController {

    private final EpisodeCrudService episodeCrudService;

    public EpisodeController(EpisodeCrudService episodeCrudService) {
        this.episodeCrudService = episodeCrudService;
    }

    @GetMapping(value = Endpoint.BY_CONTENT_ID)
    public ResponseEntity<?> getByContentId(@PathVariable Integer contentId) {
        Logger.info("Content id: " + contentId);
        return ResponseEntity.ok(episodeCrudService.getByContentId(contentId));
    }
}
