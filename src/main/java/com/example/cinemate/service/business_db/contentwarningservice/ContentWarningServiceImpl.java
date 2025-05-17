package com.example.cinemate.service.business_db.contentwarningservice;

import com.example.cinemate.dao.contentwarning.ContentWarningRepository;
import com.example.cinemate.model.db.ContentWarning;
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
@CacheConfig(cacheNames = "contentWarning")
public class ContentWarningServiceImpl implements ContentWarningService {

    private final ContentWarningRepository contentWarningRepository;
    private final CacheManager cacheManager;

    public ContentWarningServiceImpl(ContentWarningRepository contentWarningRepository, CacheManager cacheManager) {
        this.contentWarningRepository = contentWarningRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public void save(ContentWarning contentWarning) {
        contentWarningRepository.save(contentWarning);
    }

    @Override
    public void saveContentWarningsList(List<ContentWarning> contentWarnings) {
        contentWarningRepository.saveAll(contentWarnings);
        contentWarnings.stream()
                .map(cw -> cw.getContent().getId())
                .distinct()
                .forEach(contentId -> Objects.requireNonNull(cacheManager.getCache("contentWarning")).evict(contentId));
    }

    @Override
    public void update(ContentWarning contentWarning) {
        contentWarningRepository.save(contentWarning);
    }

    @Override
    public void delete(ContentWarning contentWarning) {
        contentWarningRepository.delete(contentWarning);
    }

    @Override
    public List<ContentWarning> findAll() {
        return contentWarningRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentWarningRepository.deleteAll();
    }

    @Override
    @Cacheable(key = "#contentId")
    public List<Integer> getIdWarnings(Integer contentId) {
        return contentWarningRepository.getIdWarnings(contentId);
    }

    @Override
    public List<ContentWarning> findAllByContentIds(List<Integer> ids) {
        return contentWarningRepository.findAllByContentIds(ids);
    }

    @Override
    @CacheEvict(key = "#contentId")
    public void deleteByContentIdAndWarningId(Integer contentId, Integer warningId) {
        contentWarningRepository.deleteByContentIdAndWarningId(contentId, warningId);
    }

    @Override
    public Map<Integer, List<Integer>> getWarningsByContentIds(List<Integer> contentIds) {
        var contentWarnings = this.findAllByContentIds(contentIds);
        return contentWarnings.stream()
                .collect(Collectors.groupingBy(
                        cw -> cw.getContent().getId(),
                        Collectors.mapping(cw -> cw.getWarning().getId(), Collectors.toList())
                ));
    }
}
