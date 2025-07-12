package com.example.cinemate.mapper;

import com.example.cinemate.dto.contentviewhistory.ContentViewHistoryDto;
import com.example.cinemate.model.db.ContentViewHistory;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class ContentViewHistoryMapper {

    private final AmazonS3Service amazonS3Service;

    public ContentViewHistoryMapper(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    public ContentViewHistoryDto toContentViewHistoryDto(final ContentViewHistory contentViewHistory) {
        var content = contentViewHistory.getContent();
        return new ContentViewHistoryDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                amazonS3Service.getCloudFrontUrl(content.getPosterUrl()),
                content.getDurationMin(),
                content.getAgeRating(),
                DateTimeUtil.formatDateTime(contentViewHistory.getViewedAt())
        );
    }
}
