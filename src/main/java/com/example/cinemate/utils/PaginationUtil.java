package com.example.cinemate.utils;

import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PaginationUtil {

    public Pageable getPageable(final PaginationSearchParamsDto paginationSearchParamsDto) {
        Sort.Direction direction = paginationSearchParamsDto.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(
                paginationSearchParamsDto.getPage(),
                paginationSearchParamsDto.getSize(),
                Sort.by(direction, paginationSearchParamsDto.getSortBy())
        );
    }
}
