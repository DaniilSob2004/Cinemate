package com.example.cinemate.dto.content;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private Integer id;
    private String name;
    private String contentType;
    private String posterUrl;
    private String trailerUrl;
    private String videoUrl;
    private String description;
    private Integer durationMin;
    private String ageRating;
    private String releaseDate;
    private List<Integer> actors;
    private List<Integer> genres;
    private List<Integer> warnings;
}
