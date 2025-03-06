package com.example.cinemate.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String username;
    private String firstname;
    private String surname;

    @NotBlank(message = "Email should not be blank")
    @Email
    private String email;

    private String phoneNum;
    private String avatar;
}
