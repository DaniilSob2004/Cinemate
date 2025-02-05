package com.example.cinemate.dto.auth;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserJwtDto {
    private String username;
    private List<String> roles;
    private String email;
}
