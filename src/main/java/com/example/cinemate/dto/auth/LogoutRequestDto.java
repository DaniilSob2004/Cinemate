package com.example.cinemate.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {
    String refreshToken;
}
