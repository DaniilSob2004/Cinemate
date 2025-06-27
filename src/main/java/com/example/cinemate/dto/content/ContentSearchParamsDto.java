package com.example.cinemate.dto.content;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "DTO containing parameters for searching contents, including pagination")
public class ContentSearchParamsDto extends PaginationSearchParamsDto {

    @Schema(example = "2")
    private Integer typeId = null;

    @Schema(example = "937")
    private Integer genreId;

    @Schema(example = "dune")
    private String searchStr = "";

    @Schema(example = "true")
    private Boolean isActive = true;
}
