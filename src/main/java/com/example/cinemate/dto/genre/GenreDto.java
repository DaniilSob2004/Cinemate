package com.example.cinemate.dto.genre;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    private String imageUrl;
    private String description;
    private String tags;
}
