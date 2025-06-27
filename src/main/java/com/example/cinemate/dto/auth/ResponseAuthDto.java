package com.example.cinemate.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAuthDto {
    private String accessToken;
    private String refreshToken;
}
