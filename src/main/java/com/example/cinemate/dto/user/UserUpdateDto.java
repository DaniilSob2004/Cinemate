package com.example.cinemate.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    @Schema(example = "@dan")
    private String username;

    @Schema(example = "Daniil")
    private String firstname;

    @Schema(example = "Soboliev")
    private String surname;

    @Schema(example = "dan@gmail.com")
    @NotBlank(message = "Email should not be blank")
    @Email
    private String email;

    @Schema(example = "+380685445565")
    private String phoneNum;
}
