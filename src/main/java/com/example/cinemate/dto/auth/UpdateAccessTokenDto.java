package com.example.cinemate.dto.auth;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccessTokenDto {

    @NotBlank(message = "Access token should not be blank")
    private String accessToken;
}
