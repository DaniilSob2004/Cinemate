package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.service.business.content.ContentAdminCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.CONTENTS)
public class ContentAdminController {

    private final ContentAdminCrudService contentAdminCrudService;

    public ContentAdminController(ContentAdminCrudService contentAdminCrudService) {
        this.contentAdminCrudService = contentAdminCrudService;
    }

    @GetMapping
    public ResponseEntity<?> get(@Valid ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get contents for admin (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentAdminCrudService.getListContents(contentSearchParamsDto));
    }

    @GetMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Logger.info("Get content for admin by id: " + id);
        var contentFullAdminDto = contentAdminCrudService.getById(id);
        return ResponseEntity.ok(contentFullAdminDto);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        Logger.info("-------- Add content for admin (" + contentFullAdminDto + ") --------");
        contentAdminCrudService.add(contentFullAdminDto);
        return ResponseEntity.ok("Content added successfully");
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        Logger.info("-------- Update content for admin by id: " + id + " - (" + contentFullAdminDto + ") --------");
        contentAdminCrudService.updateById(id, contentFullAdminDto);
        return ResponseEntity.ok("Content updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        Logger.info("Delete content for admin by id: " + id);
        contentAdminCrudService.delete(id);
        return ResponseEntity.ok("Content deleted successfully");
    }
}
