package com.example.cinemate.dto.wishlist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO containing parameters for add wishlist for user")
public class WishListAddDto {

    @Schema(example = "456")
    @Min(value = 1, message = "ContentId must be at least 1")
    private Integer contentId;
}
