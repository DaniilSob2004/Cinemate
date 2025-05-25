package com.example.cinemate.mapper;

import com.example.cinemate.dto.contentviewhistory.ContentViewHistoryDto;
import com.example.cinemate.model.db.ContentViewHistory;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class ContentViewHistoryMapper {

    public ContentViewHistoryDto toContentViewHistoryDto(final ContentViewHistory contentViewHistory) {
        var content = contentViewHistory.getContent();
        return new ContentViewHistoryDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                content.getPosterUrl(),
                content.getDurationMin(),
                content.getAgeRating(),
                DateTimeUtil.formatDateTime(contentViewHistory.getViewedAt())
        );
    }
}
