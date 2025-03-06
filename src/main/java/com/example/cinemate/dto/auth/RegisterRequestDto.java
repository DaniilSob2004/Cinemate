package com.example.cinemate.dto.auth;

import com.example.cinemate.validate.annotation.PasswordMatches;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(message = "Passwords don`t match")  // кастомная
public class RegisterRequestDto {

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "The password must not be blank")
    @Size(min = 3, message = "Password must contain a minimum of 3 characters")
    private String password;

    @NotBlank(message = "Confirm the password is mandatory")
    private String confirmPassword;
}
