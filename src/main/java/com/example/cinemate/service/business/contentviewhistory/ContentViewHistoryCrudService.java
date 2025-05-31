package com.example.cinemate.service.business.contentviewhistory;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.contentviewhistory.ContentHistoryParamsDto;
import com.example.cinemate.dto.contentviewhistory.ContentViewHistoryDto;
import com.example.cinemate.mapper.ContentViewHistoryMapper;
import com.example.cinemate.model.db.ContentViewHistory;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.contentviewhistoryservice.ContentViewHistoryService;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ContentViewHistoryCrudService {

    private final ContentViewHistoryService contentViewHistoryService;
    private final ContentViewHistoryMapper contentViewHistoryMapper;
    private final AccessJwtTokenService accessJwtTokenService;

    public ContentViewHistoryCrudService(ContentViewHistoryService contentViewHistoryService, ContentViewHistoryMapper contentViewHistoryMapper, AccessJwtTokenService accessJwtTokenService) {
        this.contentViewHistoryService = contentViewHistoryService;
        this.contentViewHistoryMapper = contentViewHistoryMapper;
        this.accessJwtTokenService = accessJwtTokenService;
    }

    public PagedResponse<ContentViewHistoryDto> getByUserId(final ContentHistoryParamsDto contentHistoryParamsDto, final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        contentHistoryParamsDto.setPage(contentHistoryParamsDto.getPage() - 1);
        contentHistoryParamsDto.setUserId(appUserJwtDto.getId());

        Page<ContentViewHistory> pageContentHistories = contentViewHistoryService.getContentViewHistories(contentHistoryParamsDto);

        var contentHistoriesDto = pageContentHistories.get()
                .map(contentViewHistoryMapper::toContentViewHistoryDto)
                .toList();

        return PaginationUtil.getPagedResponse(
                contentHistoriesDto,
                pageContentHistories
        );
    }
}
