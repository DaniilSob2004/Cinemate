package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoint.API_V1)
public class PrivacyPolicyController {

    @GetMapping(value = Endpoint.PRIVACY_POLICY)
    public ResponseEntity<String> getPrivacyPolicy() {
        String privacyPolicyText = "Мой текст политики конфиденциальности...";
        return ResponseEntity.ok(privacyPolicyText);
    }
}
