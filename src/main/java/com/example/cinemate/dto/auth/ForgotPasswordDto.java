package com.example.cinemate.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used to request a password reset by providing the user's email")
public class ForgotPasswordDto {

    @Schema(description = "User's email address to receive the password reset token", example = "dan@gmail.com")
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;
}
