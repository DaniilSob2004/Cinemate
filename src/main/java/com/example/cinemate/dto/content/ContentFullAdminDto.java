package com.example.cinemate.dto.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO containing full content information")
public class ContentFullAdminDto extends ContentDto {

    @Schema(example = "343", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(example = "Dune 2")
    @NotBlank(message = "Name should not be blank")
    private String name;

    @Schema(example = "movie")
    @NotBlank(message = "Content type should not be blank")
    private String contentType;

    private String posterUrl;
    private String trailerUrl;
    private String videoUrl;

    @Schema(example = "Description for content")
    private String description;

    @Schema(example = "120")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 50000, message = "The duration should be no more than 50000 minutes")
    private Integer durationMin = 1;

    @Schema(example = "18+")
    private String ageRating;

    @Schema(example = "2025-04-17")
    private String releaseDate;

    @Schema(example = "[1, 3]")
    private List<Integer> actors;

    @Schema(example = "[2, 7, 8]")
    private List<Integer> genres;

    @Schema(example = "[1]")
    private List<Integer> warnings;

    @Schema(example = "true")
    @JsonProperty("isActive")
    private boolean isActive = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
