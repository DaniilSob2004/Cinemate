package com.example.cinemate.dto.actor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing all actor information")
public class ActorDto {

    @Schema(example = "444", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "Tom")
    @NotBlank(message = "Name should not be blank")
    private String name;

    @Schema(example = "Holland")
    @NotBlank(message = "Surname should not be blank")
    private String surname;

    @Schema(example = "Some biography by this actor...")
    private String biography;
}
