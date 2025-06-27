package com.example.cinemate.dto.warning;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing all warning information")
public class WarningDto {

    @Schema(example = "222", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "violence")
    @NotBlank(message = "Name should not be blank")
    private String name;
}
