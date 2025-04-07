package com.example.cinemate.service.business_db.actorservice;

import com.example.cinemate.model.db.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorService {
    void save(Actor actor);
    void saveActorsList(List<Actor> actors);
    void update(Actor actor);
    void delete(Actor actor);
    List<Actor> findAll();
    void deleteAll();

    Optional<Actor> findByNameAndSurname(String name, String surname);
}
