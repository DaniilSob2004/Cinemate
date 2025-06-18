package com.example.cinemate.mapper.content;

import com.example.cinemate.dto.content.*;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ContentMapper {

    private final AmazonS3Service amazonS3Service;

    public ContentMapper(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    public ContentDto toContentDto(final Content content) {
        return new ContentDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                amazonS3Service.getCloudFrontUrl(content.getPosterUrl()),
                amazonS3Service.getCloudFrontUrl(content.getTrailerUrl()),
                amazonS3Service.getCloudFrontUrl(content.getVideoUrl()),
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
                amazonS3Service.getCloudFrontUrl(content.getPosterUrl()),
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
                amazonS3Service.extractKeyFromUrl(contentFullAdminDto.getPosterUrl()),
                amazonS3Service.extractKeyFromUrl(contentFullAdminDto.getTrailerUrl()),
                amazonS3Service.extractKeyFromUrl(contentFullAdminDto.getVideoUrl()),
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
                amazonS3Service.getCloudFrontUrl(content.getPosterUrl()),
                amazonS3Service.getCloudFrontUrl(content.getTrailerUrl()),
                amazonS3Service.getCloudFrontUrl(content.getVideoUrl()),
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
