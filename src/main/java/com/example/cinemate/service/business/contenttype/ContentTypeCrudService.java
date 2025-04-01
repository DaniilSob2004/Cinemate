package com.example.cinemate.service.business.contenttype;

import com.example.cinemate.dto.contenttype.ContentTypeDto;
import com.example.cinemate.exception.contenttype.ContentTypeAlreadyExists;
import com.example.cinemate.mapper.ContentTypeMapper;
import com.example.cinemate.model.db.ContentType;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentTypeCrudService {

    private final ContentTypeService contentTypeService;
    private final ContentTypeMapper contentTypeMapper;

    public ContentTypeCrudService(ContentTypeService contentTypeService, ContentTypeMapper contentTypeMapper) {
        this.contentTypeService = contentTypeService;
        this.contentTypeMapper = contentTypeMapper;
    }

    public List<ContentTypeDto> getAll() {
        return contentTypeService.findAll().stream()
                .map(contentTypeMapper::toContentTypeDto)
                .toList();
    }

    public void add(final ContentTypeDto contentTypeDto) {
        contentTypeDto.setName(contentTypeDto.getName().toLowerCase());

        contentTypeService.findByName(contentTypeDto.getName())
                .ifPresent(content -> {
                    throw new ContentTypeAlreadyExists("Content type '" + contentTypeDto.getName() + "' already exists");
                });

        contentTypeService.save(contentTypeMapper.toContentType(contentTypeDto));
    }
}
