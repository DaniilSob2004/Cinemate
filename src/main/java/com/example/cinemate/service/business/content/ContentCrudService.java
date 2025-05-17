package com.example.cinemate.service.business.content;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.content.*;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.mapper.content.ContentEnrichMapper;
import com.example.cinemate.mapper.content.ContentMapper;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContentCrudService {

    private final ContentService contentService;
    private final GenreService genreService;
    private final ContentTypeService contentTypeService;
    private final ContentMapper contentMapper;
    private final ContentEnrichMapper contentEnrichMapper;

    public ContentCrudService(ContentService contentService, GenreService genreService, ContentTypeService contentTypeService, ContentMapper contentMapper, ContentEnrichMapper contentEnrichMapper) {
        this.contentService = contentService;
        this.genreService = genreService;
        this.contentTypeService = contentTypeService;
        this.contentMapper = contentMapper;
        this.contentEnrichMapper = contentEnrichMapper;
    }

    public List<ContentDto> getRandom(final ContentRandomRequestDto contentRandomRequestDto) {
        long totalElements = contentService.getTotalContents();
        int maxPage = (int) (totalElements / contentRandomRequestDto.getCount());

        var contentSearchParamsDto = ContentSearchParamsDto.builder()
                .typeId(null)
                .genreId(null)
                .searchStr("")
                .isActive(true)
                .page(GenerateUtil.getRandomInteger(0, Math.max(1, maxPage)))
                .size(contentRandomRequestDto.getCount())
                .sortBy("id")
                .isAsc(GenerateUtil.getRandomBoolean())
                .build();

        Page<Content> pageContents = contentService.getContents(contentSearchParamsDto);
        var contentsDto = new ArrayList<>(this.mapContentToDtoWithDetails(pageContents));

        Collections.shuffle(contentsDto);

        return contentsDto;
    }

    public PagedResponse<ContentDto> getByGenre(final ContentSearchParamsDto contentSearchParamsDto) {
        genreService.findById(contentSearchParamsDto.getGenreId())
                .orElseThrow(() -> new ContentNotFoundException("Genre with id '" + contentSearchParamsDto.getGenreId() + "' not found"));

        return this.getPagedContents(contentSearchParamsDto);
    }

    public PagedResponse<ContentDto> getByType(final ContentSearchParamsDto contentSearchParamsDto) {
        contentTypeService.findById(contentSearchParamsDto.getTypeId())
                .orElseThrow(() -> new ContentNotFoundException("Content type with id '" + contentSearchParamsDto.getTypeId() + "' not found"));

        return this.getPagedContents(contentSearchParamsDto);
    }

    private PagedResponse<ContentDto> getPagedContents(ContentSearchParamsDto dto) {
        dto.setPage(dto.getPage() - 1);
        Page<Content> pageContents = contentService.getContents(dto);

        return PaginationUtil.getPagedResponse(
                this.mapContentToDtoWithDetails(pageContents),
                pageContents
        );
    }

    private List<ContentDto> mapContentToDtoWithDetails(final Page<Content> pageContents) {
        var contents = pageContents.getContent();
        var contentsDto = contents.stream().map(contentMapper::toContentDto).toList();
        contentEnrichMapper.enrichContentDto(contentsDto);
        return contentsDto;
    }
}
