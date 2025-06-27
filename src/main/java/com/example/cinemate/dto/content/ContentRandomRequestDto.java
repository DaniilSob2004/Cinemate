package com.example.cinemate.dto.content;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing parameters for searching contents by random, including pagination")
public class ContentRandomRequestDto {

    @Schema(example = "5")
    @Min(value = 1, message = "Count must be at least 1")
    @Max(value = 50, message = "The count should be no more than 50")
    private Integer count = 10;
}
