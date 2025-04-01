package com.example.cinemate.service.business_db.contenttypeservice;

import com.example.cinemate.dao.contenttype.ContentTypeRepository;
import com.example.cinemate.model.db.ContentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentTypeServiceImpl implements ContentTypeService {

    private final ContentTypeRepository contentTypeRepository;

    public ContentTypeServiceImpl(ContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    @Override
    public void save(ContentType contentType) {
        contentTypeRepository.save(contentType);
    }

    @Override
    public void saveContentTypesList(List<ContentType> contentTypes) {
        contentTypeRepository.saveAll(contentTypes);
    }

    @Override
    public void update(ContentType contentType) {
        contentTypeRepository.save(contentType);
    }

    @Override
    public void delete(ContentType contentType) {
        contentTypeRepository.delete(contentType);
    }

    @Override
    public List<ContentType> findAll() {
        return contentTypeRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentTypeRepository.deleteAll();
    }
}
