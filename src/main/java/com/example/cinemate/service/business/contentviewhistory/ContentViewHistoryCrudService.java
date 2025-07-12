package com.example.cinemate.service.business.contentviewhistory;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.contentviewhistory.*;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.mapper.ContentViewHistoryMapper;
import com.example.cinemate.model.db.ContentViewHistory;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.contentviewhistoryservice.ContentViewHistoryService;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ContentViewHistoryCrudService {

    private final ContentViewHistoryService contentViewHistoryService;
    private final AppUserService appUserService;
    private final ContentService contentService;
    private final ContentViewHistoryMapper contentViewHistoryMapper;
    private final AccessJwtTokenService accessJwtTokenService;

    public ContentViewHistoryCrudService(ContentViewHistoryService contentViewHistoryService, AppUserService appUserService, ContentService contentService, ContentViewHistoryMapper contentViewHistoryMapper, AccessJwtTokenService accessJwtTokenService) {
        this.contentViewHistoryService = contentViewHistoryService;
        this.appUserService = appUserService;
        this.contentService = contentService;
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

    public void add(final ContentViewAddDto contentViewAddDto, final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        if (!appUserService.existsById(appUserJwtDto.getId())) {
            throw new UserNotFoundException("User with id " + appUserJwtDto.getId() + " not found");
        }

        if (!contentService.existsById(contentViewAddDto.getContentId())) {
            throw new ContentNotFoundException("Content with id " + contentViewAddDto.getContentId() + " not found");
        }

        if (contentViewHistoryService.existsByUserIdAndContentId(appUserJwtDto.getId(), contentViewAddDto.getContentId())) {
            throw new ContentAlreadyExists("Content already exists in contentView");
        }

        var addContentView = contentViewHistoryMapper.toContentViewHistory(appUserJwtDto.getId(), contentViewAddDto.getContentId());
        contentViewHistoryService.save(addContentView);
    }
}
