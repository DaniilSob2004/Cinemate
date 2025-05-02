package com.example.cinemate.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentFullAdminDto {
    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotBlank(message = "Content type should not be blank")
    private String contentType;

    private String posterUrl;
    private String trailerUrl;
    private String videoUrl;
    private String description;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 50000, message = "The duration should be no more than 50000 minutes")
    private Integer durationMin = 1;

    private String ageRating;
    private String releaseDate;
    private List<Integer> actors;
    private List<Integer> genres;
    private List<Integer> warnings;

    @JsonProperty("isActive")
    private boolean isActive = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
