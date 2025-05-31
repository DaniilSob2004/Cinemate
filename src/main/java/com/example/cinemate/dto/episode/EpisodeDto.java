package com.example.cinemate.dto.episode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDto {
    private Integer id;
    private String name;
    private Integer content_id;
    private int seasonNumber;
    private int episodeNumber;
    private int durationMin;
    private String description;
    private String trailerUrl;
    private String videoUrl;
    private String releaseDate;
}
