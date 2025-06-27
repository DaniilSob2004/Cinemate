package com.example.cinemate.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaginationSearchParamsDto {

    @Schema(example = "1")
    @Min(value = 1, message = "Page number is not valid")
    private int page = 1;

    @Schema(example = "5")
    @Min(value = 1, message = "Size must be greater than or equal to 1")
    @Max(value = 50, message = "Size must be less than or equal to 50")
    private int size = 10;

    @Schema(example = "id")
    private String sortBy = "id";

    @Schema(example = "true")
    private Boolean isAsc = true;
}
