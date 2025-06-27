package com.example.cinemate.dto.genre;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing the user's test genres for users and content")
public class GenreRecTestDto {

    @Schema(description = "List genre id`s", example = "[4, 836, 99]")
    private List<Integer> genreIds;
}
