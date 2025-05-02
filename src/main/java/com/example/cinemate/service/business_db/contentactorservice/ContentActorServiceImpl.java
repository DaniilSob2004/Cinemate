package com.example.cinemate.service.business_db.contentactorservice;

import com.example.cinemate.dao.contentactor.ContentActorRepository;
import com.example.cinemate.model.db.ContentActor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentActorServiceImpl implements ContentActorService {

    private final ContentActorRepository contentActorRepository;

    public ContentActorServiceImpl(ContentActorRepository contentActorRepository) {
        this.contentActorRepository = contentActorRepository;
    }

    @Override
    public void save(ContentActor contentActor) {
        contentActorRepository.save(contentActor);
    }

    @Override
    public void saveContentActorsList(List<ContentActor> contentActors) {
        contentActorRepository.saveAll(contentActors);
    }

    @Override
    public void update(ContentActor contentActor) {
        contentActorRepository.save(contentActor);
    }

    @Override
    public void delete(ContentActor contentActor) {
        contentActorRepository.delete(contentActor);
    }

    @Override
    public List<ContentActor> findAll() {
        return contentActorRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentActorRepository.deleteAll();
    }
}
