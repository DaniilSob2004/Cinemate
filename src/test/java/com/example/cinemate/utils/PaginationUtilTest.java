package com.example.cinemate.utils;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationUtilTest {

    @Test
    void getPageable_ShouldReturnCorrectAscSort() {
        var dto = new PaginationSearchParamsDto(1, 20, "name", true);
        Pageable pageable = PaginationUtil.getPageable(dto);

        assertEquals(1, pageable.getPageNumber());
        assertEquals(20, pageable.getPageSize());
        assertEquals("name: ASC", pageable.getSort().toString());
    }

    @Test
    void getPageable_ShouldReturnCorrectDescSort() {
        var dto = new PaginationSearchParamsDto(0, 10, "createdAt", false);
        Pageable pageable = PaginationUtil.getPageable(dto);

        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals("createdAt: DESC", pageable.getSort().toString());
    }

    @Test
    void getPageable_ShouldHandleEmptySortBy() {
        var dto = new PaginationSearchParamsDto(0, 10, "", true);
        assertThrows(IllegalArgumentException.class, () -> PaginationUtil.getPageable(dto));
    }


    @Test
    void getPagedResponse_ShouldReturnCorrectPagedResponse() {
        // данные для текущей страницы
        List<String> dataOnPage = List.of("apple", "banana");

        // все данные, имитируют 5 элементов
        List<String> allData = List.of("apple", "banana", "cherry", "date", "elderberry");

        // объект Page (текущая страница 0, размер 2, всего элементов 5)
        Page<String> page = new PageImpl<>(
                dataOnPage,
                PageRequest.of(0, 2),
                allData.size()
        );

        PagedResponse<String> response = PaginationUtil.getPagedResponse(dataOnPage, page);

        assertEquals(dataOnPage, response.getData());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertEquals(1, response.getCurrentPage());
        assertEquals(2, response.getPageSize());
    }

    @Test
    void getPagedResponse_ShouldHandleEmptyData() {
        List<String> dataOnPage = List.of();
        Page<String> page = new PageImpl<>(
                dataOnPage,
                PageRequest.of(2, 3),
                0
        );

        PagedResponse<String> response = PaginationUtil.getPagedResponse(dataOnPage, page);

        assertEquals(0, response.getData().size());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertEquals(3, response.getPageSize());
        assertEquals(3, response.getCurrentPage());
    }
}