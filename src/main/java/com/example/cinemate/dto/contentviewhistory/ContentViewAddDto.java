package com.example.cinemate.dto.contentviewhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing parameters for add content view for user")
public class ContentViewAddDto {

    @Schema(example = "417")
    @Min(value = 1, message = "ContentId must be at least 1")
    private Integer contentId;
}
