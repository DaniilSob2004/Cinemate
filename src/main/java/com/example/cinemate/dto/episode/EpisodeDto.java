package com.example.cinemate.dto.episode;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDto {
    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @Min(value = 1, message = "Content id must be at least 1")
    private Integer contentId;

    @Min(value = 1, message = "Season number must be at least 1")
    @Max(value = 100, message = "The duration should be no more than 100")
    private int seasonNumber;

    @Min(value = 1, message = "Episode number must be at least 1")
    @Max(value = 1000, message = "The duration should be no more than 1000")
    private int episodeNumber;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 50000, message = "The duration should be no more than 50000 minutes")
    private int durationMin;

    private String description;
    private String trailerUrl;
    private String videoUrl;
    private String releaseDate;
}
