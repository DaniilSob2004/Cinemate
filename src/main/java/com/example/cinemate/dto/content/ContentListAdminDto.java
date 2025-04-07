package com.example.cinemate.dto.content;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentListAdminDto {
    private Integer id;
    private String name;
    private String contentType;
    private String posterUrl;
    private String ageRating;
    private boolean isActive;
    private LocalDateTime createdAt;
}
