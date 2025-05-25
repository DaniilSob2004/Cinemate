package com.example.cinemate.mapper.content;

import com.example.cinemate.dto.content.*;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContentMapper {

    public ContentDto toContentDto(final Content content) {
        return new ContentDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                content.getPosterUrl(),
                content.getTrailerUrl(),
                content.getVideoUrl(),
                content.getDescription(),
                content.getDurationMin(),
                content.getAgeRating(),
                DateTimeUtil.formatDate(content.getReleaseDate()),
                null,
                null,
                null
        );
    }

    public ContentListAdminDto toContentListAdminDto(final Content content) {
        return new ContentListAdminDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                content.getPosterUrl(),
                content.getAgeRating(),
                content.isActive(),
                content.getCreatedAt()
        );
    }

    public Content toContent(final ContentFullAdminDto contentFullAdminDto) {
        return new Content(
                contentFullAdminDto.getId(),
                contentFullAdminDto.getName(),
                null,
                contentFullAdminDto.getPosterUrl(),
                contentFullAdminDto.getTrailerUrl(),
                contentFullAdminDto.getVideoUrl(),
                contentFullAdminDto.getDescription(),
                contentFullAdminDto.getDurationMin(),
                contentFullAdminDto.getAgeRating(),
                LocalDate.parse(contentFullAdminDto.getReleaseDate()),
                contentFullAdminDto.isActive(),
                contentFullAdminDto.getCreatedAt(),
                contentFullAdminDto.getUpdatedAt(),
                null
        );
    }

    public ContentFullAdminDto toContentFullAdminDto(final Content content) {
        return new ContentFullAdminDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                content.getPosterUrl(),
                content.getTrailerUrl(),
                content.getVideoUrl(),
                content.getDescription(),
                content.getDurationMin(),
                content.getAgeRating(),
                DateTimeUtil.formatDate(content.getReleaseDate()),
                null,
                null,
                null,
                content.isActive(),
                content.getCreatedAt(),
                content.getUpdatedAt()
        );
    }
}
