package com.example.cinemate.dto.auth;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserJwtDto {
    private Integer id;
    private String email;
    private List<String> roles;
}
