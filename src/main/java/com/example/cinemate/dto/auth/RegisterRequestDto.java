package com.example.cinemate.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String email;
    private String password;
    private String confirmPassword;
}
