package com.example.cinemate.mapper;

import com.example.cinemate.dto.contenttype.ContentTypeDto;
import com.example.cinemate.model.db.ContentType;
import org.springframework.stereotype.Component;

@Component
public class ContentTypeMapper {

    public ContentTypeDto toContentTypeDto(final ContentType contentType) {
        return new ContentTypeDto(
                contentType.getId(),
                contentType.getName(),
                contentType.getDescription(),
                contentType.getTags()
        );
    }

    public ContentType toContentType(final ContentTypeDto contentTypeDto) {
        return new ContentType(
                contentTypeDto.getId(),
                contentTypeDto.getName(),
                contentTypeDto.getDescription(),
                contentTypeDto.getTags()
        );
    }
}
