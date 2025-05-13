package com.example.cinemate.service.business.content;

import com.example.cinemate.dto.content.ContentDto;
import com.example.cinemate.dto.content.ContentRandomRequestDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.mapper.ContentMapper;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.utils.GenerateUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentCrudService {

    private final ContentService contentService;
    private final ContentMapper contentMapper;

    public ContentCrudService(ContentService contentService, ContentMapper contentMapper) {
        this.contentService = contentService;
        this.contentMapper = contentMapper;
    }

    public List<ContentDto> getRandom(final ContentRandomRequestDto contentRandomRequestDto) {
        long totalElements = contentService.getTotalContents();
        int maxPage = (int) (totalElements / contentRandomRequestDto.getCount());

        var contentSearchParamsDto = ContentSearchParamsDto.builder()
                .contentTypeId(null)
                .searchStr("")
                .isActive(true)
                .page(GenerateUtil.getRandomInteger(0, Math.max(1, maxPage)))
                .size(contentRandomRequestDto.getCount())
                .sortBy("id")
                .isAsc(GenerateUtil.getRandomBoolean())
                .build();

        Page<Content> pageContents = contentService.getContents(contentSearchParamsDto);
        List<ContentDto> contentsDto = new ArrayList<>(pageContents.get()
                .map(contentMapper::toContentDto)
                .toList());
        Collections.shuffle(contentsDto);
        
        return contentsDto;
    }
}
