package com.example.cinemate.dto.user;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Schema(description = "DTO containing parameters for searching users, including pagination")
public class UserSearchParamsDto extends PaginationSearchParamsDto {

    @Schema(example = "daniil")
    private String searchStr = "";
}
