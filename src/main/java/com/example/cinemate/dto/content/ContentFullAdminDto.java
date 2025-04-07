package com.example.cinemate.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentFullAdminDto {
    private Integer id;
    private String name;
    private String contentType;
    private String posterUrl;
    private String trailerUrl;
    private String videoUrl;
    private String description;
    private String durationMin;
    private String ageRating;
    private LocalDate releaseDate;
    private List<String> actors;
    private List<String> genres;
    private List<String> warnings;

    @JsonProperty("isActive")
    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
