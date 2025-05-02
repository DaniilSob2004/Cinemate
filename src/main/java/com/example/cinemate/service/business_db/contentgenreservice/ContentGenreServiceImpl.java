package com.example.cinemate.service.business_db.contentgenreservice;

import com.example.cinemate.dao.contentgenre.ContentGenreRepository;
import com.example.cinemate.model.db.ContentGenre;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@CacheConfig(cacheNames = "contentGenre")
public class ContentGenreServiceImpl implements ContentGenreService {

    private final ContentGenreRepository contentGenreRepository;
    private final CacheManager cacheManager;

    public ContentGenreServiceImpl(ContentGenreRepository contentGenreRepository, CacheManager cacheManager) {
        this.contentGenreRepository = contentGenreRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public void save(ContentGenre contentGenre) {
        contentGenreRepository.save(contentGenre);
    }

    @Override
    public void saveContentGenresList(List<ContentGenre> contentGenres) {
        contentGenreRepository.saveAll(contentGenres);
        contentGenres.stream()
                .map(cg -> cg.getContent().getId())
                .distinct()
                .forEach(contentId -> Objects.requireNonNull(cacheManager.getCache("contentGenre")).evict(contentId));
    }

    @Override
    public void update(ContentGenre contentGenre) {
        contentGenreRepository.save(contentGenre);
    }

    @Override
    public void delete(ContentGenre contentGenre) {
        contentGenreRepository.delete(contentGenre);
    }

    @Override
    public List<ContentGenre> findAll() {
        return contentGenreRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentGenreRepository.deleteAll();
    }

    @Override
    @Cacheable(key = "#contentId")
    public List<Integer> getIdGenres(Integer contentId) {
        return contentGenreRepository.getIdGenres(contentId);
    }

    @Override
    @CacheEvict(key = "#contentId")
    public void deleteByContentIdAndGenreId(Integer contentId, Integer genreId) {
        contentGenreRepository.deleteByContentIdAndGenreId(contentId, genreId);
    }
}
