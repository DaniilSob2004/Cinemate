package com.example.cinemate.dto.wishlist;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistDto {
    private Integer contentId;
    private String contentName;
    private String contentType;
    private String posterUrl;
    private Integer durationMin;
    private String ageRating;
    private String addedAt;
}
