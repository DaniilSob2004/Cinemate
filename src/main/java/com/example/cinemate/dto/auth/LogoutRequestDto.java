package com.example.cinemate.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for logout user using a valid refresh token")
public class LogoutRequestDto {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @NotBlank(message = "Refresh token should not be blank")
    private String refreshToken;
}
