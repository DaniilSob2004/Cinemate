package com.example.cinemate.dto.auth;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequestDto {

    @NotBlank(message = "Token should not be blank")
    private String token;

    @Size(min = 3, message = "Password must contain a minimum of 3 characters")
    private String newPassword;
}
