package com.example.cinemate.mapper.episode;

import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.model.db.Episode;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Component
public class EpisodeMapper {

    private final AmazonS3Service amazonS3Service;
    private final EntityManager entityManager;

    public EpisodeMapper(AmazonS3Service amazonS3Service, EntityManager entityManager) {
        this.amazonS3Service = amazonS3Service;
        this.entityManager = entityManager;
    }

    public EpisodeDto toEpisodeDto(final Episode episode) {
        return new EpisodeDto(
                episode.getId(),
                episode.getName(),
                episode.getContent().getId(),
                episode.getSeasonNumber(),
                episode.getEpisodeNumber(),
                episode.getDurationMin(),
                episode.getDescription(),
                amazonS3Service.getCloudFrontUrl(episode.getTrailerUrl()),
                amazonS3Service.getCloudFrontUrl(episode.getVideoUrl()),
                DateTimeUtil.formatDate(episode.getReleaseDate())
        );
    }

    public Episode toEpisode(final EpisodeDto episodeDto) {
        // не загружаем Content из БД, а создаём прокси-ссылку
        Content contentRef;
        try {
            contentRef = entityManager.getReference(Content.class, episodeDto.getContentId());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("Content with id " + episodeDto.getContentId() + " not found");
        }

        return new Episode(
                episodeDto.getId(),
                episodeDto.getName(),
                contentRef,
                episodeDto.getSeasonNumber(),
                episodeDto.getEpisodeNumber(),
                episodeDto.getDurationMin(),
                episodeDto.getDescription(),
                episodeDto.getTrailerUrl(),
                episodeDto.getVideoUrl(),
                LocalDate.parse(episodeDto.getReleaseDate()),
                null
        );
    }
}
