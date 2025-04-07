package com.example.cinemate.dto.actor;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {

    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotBlank(message = "Surname should not be blank")
    private String surname;

    private String biography;
}
