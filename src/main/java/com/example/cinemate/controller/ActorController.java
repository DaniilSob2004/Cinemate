package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.actor.ActorDto;
import com.example.cinemate.service.business.actor.ActorCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ACTORS)
@Tag(name = "Actor Admin", description = "Actor management for admin")
public class ActorController {

    private final ActorCrudService actorCrudService;

    public ActorController(ActorCrudService actorCrudService) {
        this.actorCrudService = actorCrudService;
    }

    @GetMapping(value = Endpoint.ALL)
    @Operation(summary = "Get all actors", description = "Get all actors and return List(ActorDto)")
    public ResponseEntity<?> getAll() {
        List<ActorDto> actors = actorCrudService.getAll();
        Logger.info("Successfully retrieved " + actors.size() + " actors");
        return ResponseEntity.ok(actors);
    }

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add actor", description = "Add actor by ActorDto")
    public ResponseEntity<?> add(@Valid @RequestBody ActorDto actorDto) {
        Logger.info("-------- Add actor (" + actorDto + ") --------");
        actorCrudService.add(actorDto);
        return ResponseEntity.ok("Actor added successfully");
    }
}
