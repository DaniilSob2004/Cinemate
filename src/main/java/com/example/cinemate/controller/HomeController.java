package com.example.cinemate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

@RestController
@RequestMapping(value = "/api/v2/secure")
public class HomeController {

    @GetMapping(value = "/photo")
    public ResponseEntity<?> getPhoto(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Logger.info("Token: " + token);

        return ResponseEntity.ok("Your photo for auth user!");
    }
}
