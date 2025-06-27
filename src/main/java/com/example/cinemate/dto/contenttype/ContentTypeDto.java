package com.example.cinemate.dto.contenttype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing all contentType information")
public class ContentTypeDto {

    @Schema(example = "111", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "movie")
    @NotBlank(message = "Name should not be blank")
    private String name;

    @Schema(example = "Description by contentType")
    private String description;

    @Schema(example = "#tag1,#tag2")
    private String tags;
}
