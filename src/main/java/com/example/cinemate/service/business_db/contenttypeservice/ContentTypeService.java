package com.example.cinemate.service.business_db.contenttypeservice;

import com.example.cinemate.model.db.ContentType;

import java.util.List;

public interface ContentTypeService {
    void save(ContentType contentType);
    void saveContentTypesList(List<ContentType> contentTypes);
    void update(ContentType contentType);
    void delete(ContentType contentType);
    List<ContentType> findAll();
    void deleteAll();
}
