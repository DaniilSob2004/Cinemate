package com.example.cinemate.dto.contentviewhistory;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentViewHistoryDto {
    private Integer contentId;
    private String contentName;
    private String contentType;
    private String posterUrl;
    private Integer durationMin;
    private String ageRating;
    private String viewedAt;
}
