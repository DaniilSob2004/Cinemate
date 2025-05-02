package com.example.cinemate.service.business_db.contentactorservice;

import com.example.cinemate.model.db.Content;
import com.example.cinemate.model.db.ContentActor;

import java.util.List;

public interface ContentActorService {
    void save(ContentActor contentActor);
    void saveContentActorsList(List<ContentActor> contentActors);
    void update(ContentActor contentActor);
    void delete(ContentActor contentActor);
    List<ContentActor> findAll();
    void deleteAll();

    List<Integer> getIdActors(Integer contentId);
    void deleteByContentIdAndActorId(Integer contentId, Integer actorId);
}
