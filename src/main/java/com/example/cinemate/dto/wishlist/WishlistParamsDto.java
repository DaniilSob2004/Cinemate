package com.example.cinemate.dto.wishlist;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "DTO containing parameters for searching wishlists, including pagination and user id")
public class WishlistParamsDto extends PaginationSearchParamsDto {

    @Schema(example = "42")
    private Integer userId;
}
