package com.example.cinemate.dto.content;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentByGenreRequestDto {

    @Min(value = 1, message = "Genres id must be at least 1")
    private Integer id = 1;

    @Min(value = 1, message = "Count must be at least 1")
    @Max(value = 50, message = "The count should be no more than 50")
    private Integer count = 10;
}
