package com.example.cinemate.dto.episode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing episode information")
public class EpisodeDto {

    @Schema(example = "777", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "Episode 1")
    @NotBlank(message = "Name should not be blank")
    private String name;

    @Schema(example = "2")
    @Min(value = 1, message = "Content id must be at least 1")
    private Integer contentId;

    @Schema(example = "1")
    @Min(value = 1, message = "Season number must be at least 1")
    @Max(value = 100, message = "The duration should be no more than 100")
    private int seasonNumber;

    @Schema(example = "3")
    @Min(value = 1, message = "Episode number must be at least 1")
    @Max(value = 1000, message = "The duration should be no more than 1000")
    private int episodeNumber;

    @Schema(example = "120")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 50000, message = "The duration should be no more than 50000 minutes")
    private int durationMin;

    @Schema(example = "Some description for episode")
    private String description;

    private String trailerUrl;
    private String videoUrl;

    @Schema(example = "2025-04-17")
    private String releaseDate;
}
