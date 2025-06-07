package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.service.business.episode.EpisodeCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.EPISODES)
public class EpisodeAdminController {

    private final EpisodeCrudService episodeCrudService;

    public EpisodeAdminController(EpisodeCrudService episodeCrudService) {
        this.episodeCrudService = episodeCrudService;
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody EpisodeDto episodeDto) {
        Logger.info("-------- Add episode (" + episodeDto + ") --------");
        episodeCrudService.add(episodeDto);
        return ResponseEntity.ok("Episode added successfully");
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody EpisodeDto EpisodeDto) {
        Logger.info("-------- Update episode for admin by id: " + id + " - (" + EpisodeDto + ") --------");
        episodeCrudService.updateById(id, EpisodeDto);
        return ResponseEntity.ok("Episode updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Logger.info("Delete episode for admin by id: " + id);
        episodeCrudService.deleteById(id);
        return ResponseEntity.ok("Episode deleted successfully");
    }
}
