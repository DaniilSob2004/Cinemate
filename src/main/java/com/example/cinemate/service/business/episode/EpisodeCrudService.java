package com.example.cinemate.service.business.episode;

import com.example.cinemate.dto.episode.*;
import com.example.cinemate.dto.episode.file.EpisodeFilesDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.mapper.episode.EpisodeFileMapper;
import com.example.cinemate.mapper.episode.EpisodeMapper;
import com.example.cinemate.service.business.common.UploadFilesAsyncService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.episodeservice.EpisodeService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "episodes_dto")
public class EpisodeCrudService {

    private final UploadFilesAsyncService uploadFilesAsyncService;
    private final EpisodeService episodeService;
    private final ContentService contentService;
    private final EpisodeMapper episodeMapper;
    private final EpisodeFileMapper episodeFileMapper;
    private final CacheManager cacheManager;

    public EpisodeCrudService(UploadFilesAsyncService uploadFilesAsyncService, EpisodeService episodeService, ContentService contentService, EpisodeMapper episodeMapper, EpisodeFileMapper episodeFileMapper, CacheManager cacheManager) {
        this.uploadFilesAsyncService = uploadFilesAsyncService;
        this.episodeService = episodeService;
        this.contentService = contentService;
        this.episodeMapper = episodeMapper;
        this.episodeFileMapper = episodeFileMapper;
        this.cacheManager = cacheManager;
    }

    @Cacheable(key = "#contentId")
    public EpisodesWrapperDto getByContentId(final Integer contentId) {
        var episodes = episodeService.getByContentId(contentId).stream()
                .map(episodeMapper::toEpisodeDto)
                .toList();
        return new EpisodesWrapperDto(episodes);
    }

    @CacheEvict(key = "#episodeDto.contentId")
    public void add(final EpisodeDto episodeDto, final EpisodeFilesDto episodeFilesDto) {
        // есть ли такой контент
        contentService.findById(episodeDto.getContentId())
                .orElseThrow(() -> new ContentAlreadyExists("Content with id '" + episodeDto.getContentId() + "' already exists"));

        // есть ли такой эпизод
        if (episodeService.existsByContentId(episodeDto.getContentId(), episodeDto.getSeasonNumber(), episodeDto.getEpisodeNumber())) {
            throw new ContentAlreadyExists("Episode already exists for given season and episode number");
        }

        var newEpisode = episodeMapper.toEpisode(episodeDto);
        newEpisode.setCreatedAt(LocalDateTime.now());

        // сохраняем
        var savedEpisode = episodeService.save(newEpisode);

        // в отдельном потоке загружаем видео
        var episodeFilesBufferDto = episodeFileMapper.toEpisodeFilesBufferDto(episodeFilesDto);
        uploadFilesAsyncService.uploadEpisodeFilesAndUpdate(savedEpisode, episodeFilesBufferDto);
    }

    @Transactional
    @CacheEvict(key = "#episodeDto.contentId")
    public void updateById(final Integer id, final EpisodeDto episodeDto) {
        // получение эпизода по id
        var episode = episodeService.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Episode with id '" + id + "' not found"));

        // меняются ли контент, сезон или номер серии
        boolean isChanged = !episode.getContent().getId().equals(episodeDto.getContentId())
                || episode.getSeasonNumber() != episodeDto.getSeasonNumber()
                || episode.getEpisodeNumber() != episodeDto.getEpisodeNumber();

        // получение эпизода по контенту, сезону и серии
        if (isChanged) {
            // есть ли уже другой эпизод с такими параметрами
            Optional<Integer> anotherEpisodeId = episodeService
                    .getIdByContentId(
                            episodeDto.getContentId(),
                            episodeDto.getSeasonNumber(),
                            episodeDto.getEpisodeNumber()
                    );
            if (anotherEpisodeId.isPresent() && !anotherEpisodeId.get().equals(id)) {
                throw new ContentAlreadyExists("Episode already exists");
            }
        }

        var updateEpisode = episodeMapper.toEpisode(episodeDto);
        updateEpisode.setId(episode.getId());
        updateEpisode.setCreatedAt(episode.getCreatedAt());

        episodeService.update(updateEpisode);
    }

    @Transactional
    public void deleteById(final Integer id) {
        episodeService.findById(id).ifPresentOrElse(
                episode -> {
                    episodeService.delete(episode);
                    Integer contentId = episode.getContent().getId();
                    Objects.requireNonNull(cacheManager.getCache("episodesDto")).evict(contentId);
                },
                () -> {
                    throw new ContentNotFoundException("Episode with id '" + id + "' not found");
                });
    }
}
