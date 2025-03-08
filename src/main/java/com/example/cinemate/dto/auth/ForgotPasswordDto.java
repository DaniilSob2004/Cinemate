package com.example.cinemate.dto.auth;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDto {

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;
}
