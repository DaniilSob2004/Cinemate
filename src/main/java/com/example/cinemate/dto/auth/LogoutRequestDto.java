package com.example.cinemate.dto.auth;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {

    @NotBlank(message = "Refresh token should not be blank")
    private String refreshToken;
}
