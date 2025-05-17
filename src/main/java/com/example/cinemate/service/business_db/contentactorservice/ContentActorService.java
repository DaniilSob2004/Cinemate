package com.example.cinemate.service.business_db.contentactorservice;

import com.example.cinemate.model.db.ContentActor;

import java.util.List;
import java.util.Map;

public interface ContentActorService {
    void save(ContentActor contentActor);
    void saveContentActorsList(List<ContentActor> contentActors);
    void update(ContentActor contentActor);
    void delete(ContentActor contentActor);
    List<ContentActor> findAll();
    void deleteAll();

    List<Integer> getIdActors(Integer contentId);
    List<ContentActor> findAllByContentIds(List<Integer> ids);
    void deleteByContentIdAndActorId(Integer contentId, Integer actorId);

    Map<Integer, List<Integer>> getActorsByContentIds(List<Integer> contentIds);
}
