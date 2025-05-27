package com.example.cinemate.dto.wishlist;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class WishlistParamsDto extends PaginationSearchParamsDto {
    private Integer userId;
}
