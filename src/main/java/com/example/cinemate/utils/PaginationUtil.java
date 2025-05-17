package com.example.cinemate.utils;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    public <T, M> PagedResponse<T> getPagedResponse(final List<T> contents, final Page<M> pageContents) {
        return new PagedResponse<>(
                contents,
                pageContents.getTotalElements(),
                pageContents.getTotalPages(),
                pageContents.getNumber() + 1,
                pageContents.getSize()
        );
    }
}
