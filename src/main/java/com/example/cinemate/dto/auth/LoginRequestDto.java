package com.example.cinemate.dto.auth;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 3, message = "Password must contain a minimum of 3 characters")
    private String password;
}
