package com.example.cinemate.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used to reset the user's password using a reset token")
public class ResetPasswordRequestDto {

    @Schema(description = "Password reset token sent to the user's email", example = "d1f4e8b2-3c6a-4f14-9fcd-7f91e1234567")
    @NotBlank(message = "Token should not be blank")
    private String token;

    @Schema(example = "12345")
    @Size(min = 3, message = "Password must contain a minimum of 3 characters")
    private String newPassword;
}
