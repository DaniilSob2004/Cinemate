package com.example.cinemate.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating access token using a valid refresh token")
public class UpdateAccessTokenDto {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @NotBlank(message = "Access token should not be blank")
    private String accessToken;
}
