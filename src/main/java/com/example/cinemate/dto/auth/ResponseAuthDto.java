package com.example.cinemate.dto.auth;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAuthDto {
    private String accessToken;
    private String refreshToken;
}
