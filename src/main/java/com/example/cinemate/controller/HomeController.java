package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

@RestController
public class HomeController {

    @GetMapping(value = Endpoint.API_V1 + Endpoint.GET_USER_INFO)
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Logger.info("Token: " + token);

        return ResponseEntity.ok("User data for auth user!");
    }

    @GetMapping(value = Endpoint.API_V2 + Endpoint.GET_PHOTO)
    public ResponseEntity<?> getPhoto(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Logger.info("Token: " + token);

        return ResponseEntity.ok("Your photo for admin!");
    }
}
