package com.example.cinemate.dto.content;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "DTO containing parameters for searching contents by user rec, including pagination")
public class ContentRecSearchParamsDto extends PaginationSearchParamsDto {

    @Schema(
            example = "true",
            description = """
                    Flag indicating whether the recommendation search should be automatic.
                    If true, the system applies automatic filters to generate recommendations.""")
    private Boolean isAuto;

    @Schema(hidden = true)
    private List<Integer> genreIds;

    @Schema(hidden = true)
    private List<Integer> contentTypeIds;
}
