package com.example.cinemate.dto.contentviewhistory;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "DTO containing parameters for searching contentViewHistories, including pagination and user id")
public class ContentHistoryParamsDto extends PaginationSearchParamsDto {

    @Schema(example = "35")
    private Integer userId;
}
