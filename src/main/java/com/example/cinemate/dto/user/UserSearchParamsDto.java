package com.example.cinemate.dto.user;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserSearchParamsDto extends PaginationSearchParamsDto {
    private String searchStr = "";
}
