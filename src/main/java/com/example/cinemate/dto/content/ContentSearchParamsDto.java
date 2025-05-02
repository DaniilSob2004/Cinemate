package com.example.cinemate.dto.content;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ContentSearchParamsDto extends PaginationSearchParamsDto {
    private Integer contentTypeId = 1;
    private String searchStr = "";
    private Boolean isActive = true;
}
