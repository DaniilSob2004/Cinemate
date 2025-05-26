package com.example.cinemate.dto.content;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ContentRecSearchParamsDto extends PaginationSearchParamsDto {
    private List<Integer> genreIds;
    private List<Integer> contentTypeIds;
}
