package com.example.cinemate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String usernameOrId;
    private String password;
    private boolean isId;
    private String provider;
}
