package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.authprovider.AuthProviderDto;
import com.example.cinemate.service.business.authprovider.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.PROVIDERS)
public class AuthProviderController {

    private final ProviderService providerService;

    public AuthProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<AuthProviderDto> providers = providerService.getAll();
        Logger.info("Successfully retrieved {} providers", providers.size());
        return ResponseEntity.ok(providers);
    }
}
