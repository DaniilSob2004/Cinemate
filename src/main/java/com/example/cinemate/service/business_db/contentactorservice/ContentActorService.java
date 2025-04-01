package com.example.cinemate.service.business_db.contentactorservice;

import com.example.cinemate.model.db.ContentActor;

import java.util.List;

public interface ContentActorService {
    void save(ContentActor contentActor);
    void saveContentTypesList(List<ContentActor> contentActors);
    void update(ContentActor contentActor);
    void delete(ContentActor contentActor);
    List<ContentActor> findAll();
    void deleteAll();
}
