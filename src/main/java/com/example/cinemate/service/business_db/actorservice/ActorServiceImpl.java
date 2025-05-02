package com.example.cinemate.service.business_db.actorservice;

import com.example.cinemate.dao.actor.ActorRepository;
import com.example.cinemate.model.db.Actor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "actor")
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public void save(Actor actor) {
        actorRepository.save(actor);
    }

    @Override
    public void saveActorsList(List<Actor> actors) {
        actorRepository.saveAll(actors);
    }

    @Override
    @CachePut(key = "#actor.id")
    public void update(Actor actor) {
        actorRepository.save(actor);
    }

    @Override
    @CacheEvict(key = "#actor.id")
    public void delete(Actor actor) {
        actorRepository.delete(actor);
    }

    @Override
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    @Override
    public void deleteAll() {
        actorRepository.deleteAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<Actor> findById(Integer id) {
        return actorRepository.findActorById(id);
    }

    @Override
    public Optional<Actor> findByNameAndSurname(String name, String surname) {
        return actorRepository.findActorByNameAndSurname(name, surname);
    }
}
