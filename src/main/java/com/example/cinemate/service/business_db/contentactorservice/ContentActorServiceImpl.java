package com.example.cinemate.service.business_db.contentactorservice;

import com.example.cinemate.dao.contentactor.ContentActorRepository;
import com.example.cinemate.model.db.ContentActor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "contentActor")
public class ContentActorServiceImpl implements ContentActorService {

    private final ContentActorRepository contentActorRepository;
    private final CacheManager cacheManager;

    public ContentActorServiceImpl(ContentActorRepository contentActorRepository, CacheManager cacheManager) {
        this.contentActorRepository = contentActorRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public void save(ContentActor contentActor) {
        contentActorRepository.save(contentActor);
    }

    @Override
    public void saveContentActorsList(List<ContentActor> contentActors) {
        contentActorRepository.saveAll(contentActors);
        contentActors.stream()
                .map(ca -> ca.getContent().getId())
                .distinct()
                .forEach(contentId -> Objects.requireNonNull(cacheManager.getCache("contentActor")).evict(contentId));
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

    @Override
    @Cacheable(key = "#contentId")
    public List<Integer> getIdActors(Integer contentId) {
        return contentActorRepository.getIdActors(contentId);
    }

    @Override
    public List<ContentActor> findAllByContentIds(List<Integer> ids) {
        return contentActorRepository.findAllByContentIds(ids);
    }

    @Override
    @CacheEvict(key = "#contentId")
    public void deleteByContentIdAndActorId(Integer contentId, Integer actorId) {
       contentActorRepository.deleteByContentIdAndActorId(contentId, actorId);
    }

    @Override
    public Map<Integer, List<Integer>> getActorsByContentIds(List<Integer> contentIds) {
        var contentActors = this.findAllByContentIds(contentIds);
        return contentActors.stream()
                .collect(Collectors.groupingBy(
                        ca -> ca.getContent().getId(),
                        Collectors.mapping(ca -> ca.getActor().getId(), Collectors.toList())
                ));
    }
}
