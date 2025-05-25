package com.example.cinemate.dto.contentviewhistory;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ContentHistoryParamsDto extends PaginationSearchParamsDto {
    private Integer userId;
}
