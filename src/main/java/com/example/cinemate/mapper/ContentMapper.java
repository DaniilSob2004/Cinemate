package com.example.cinemate.mapper;

import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.dto.content.ContentListAdminDto;
import com.example.cinemate.model.db.Content;
import org.springframework.stereotype.Component;

@Component
public class ContentMapper {

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
                contentFullAdminDto.getReleaseDate(),
                contentFullAdminDto.isActive(),
                contentFullAdminDto.getCreatedAt(),
                contentFullAdminDto.getUpdatedAt()
        );
    }
}
