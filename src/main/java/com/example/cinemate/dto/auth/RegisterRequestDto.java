package com.example.cinemate.dto.auth;

import com.example.cinemate.validate.annotation.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(message = "Passwords don`t match")  // кастомная валидация
@Schema(description = "DTO used for registering a new user")
public class RegisterRequestDto {

    @Schema(example = "user@gmail.com")
    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;

    @Schema(example = "12345")
    @Size(min = 3, max = 50, message = "Password must contain a minimum of 3 and max 50 characters")
    private String password;

    @Schema(example = "12345")
    @NotBlank(message = "Confirm the password is mandatory")
    private String confirmPassword;
}
