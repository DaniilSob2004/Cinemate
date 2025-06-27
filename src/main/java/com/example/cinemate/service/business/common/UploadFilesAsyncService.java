package com.example.cinemate.service.business.common;

import com.example.cinemate.dto.content.file.ContentFilesBufferDto;
import com.example.cinemate.dto.episode.file.EpisodeFilesBufferDto;
import com.example.cinemate.dto.genre.file.GenreFilesBufferDto;
import com.example.cinemate.dto.user.file.UserFilesBufferDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.model.db.Episode;
import com.example.cinemate.model.db.Genre;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.episodeservice.EpisodeService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;

@Service
public class UploadFilesAsyncService {

    @Value("${amazon_s3.trailer_root_path_prefix}")
    private String trailerRootPathPrefix;

    @Value("${amazon_s3.genre_root_path_prefix}")
    private String genreRootPathPrefix;

    @Value("${amazon_s3.avatar_root_path_prefix}")
    private String avatarRootPathPrefix;

    private final AmazonS3Service amazonS3Service;
    private final ContentService contentService;
    private final EpisodeService episodeService;
    private final GenreService genreService;
    private final AppUserService appUserService;

    public UploadFilesAsyncService(AmazonS3Service amazonS3Service, ContentService contentService, EpisodeService episodeService, GenreService genreService, AppUserService appUserService) {
        this.amazonS3Service = amazonS3Service;
        this.contentService = contentService;
        this.episodeService = episodeService;
        this.genreService = genreService;
        this.appUserService = appUserService;
    }

    @Async
    public void uploadContentFilesAndUpdate(final Content content, final ContentFilesBufferDto contentFilesBufferDto) {
        boolean isUpdated = false;

        // загружаем трейлер и видео в s3
        if (contentFilesBufferDto.getTrailer() != null) {
            String trailerKey = amazonS3Service.uploadAndGenerateKey(contentFilesBufferDto.getTrailer(), trailerRootPathPrefix);
            content.setTrailerUrl(trailerKey);
            isUpdated = true;
        }
        if (contentFilesBufferDto.getVideo() != null) {
            String videoKey = amazonS3Service.uploadAndGenerateKey(contentFilesBufferDto.getVideo(), trailerRootPathPrefix);
            content.setVideoUrl(videoKey);
            isUpdated = true;
        }

        // если изменили
        if (isUpdated) {
            content.setUpdatedAt(LocalDateTime.now());
            contentService.save(content);
            Logger.info("S3 files have been successfully uploaded and content has been updated: " + content.getId());
        }
    }

    @Async
    public void uploadEpisodeFilesAndUpdate(final Episode episode, final EpisodeFilesBufferDto episodeFilesBufferDto) {
        boolean isUpdated = false;

        // загружаем трейлер и видео в s3
        if (episodeFilesBufferDto.getTrailer() != null) {
            String trailerKey = amazonS3Service.uploadAndGenerateKey(episodeFilesBufferDto.getTrailer(), trailerRootPathPrefix);
            episode.setTrailerUrl(trailerKey);
            isUpdated = true;
        }
        if (episodeFilesBufferDto.getVideo() != null) {
            String videoKey = amazonS3Service.uploadAndGenerateKey(episodeFilesBufferDto.getVideo(), trailerRootPathPrefix);
            episode.setVideoUrl(videoKey);
            isUpdated = true;
        }

        // если изменили
        if (isUpdated) {
            episodeService.save(episode);
            Logger.info("S3 files have been successfully uploaded and episode has been updated: " + episode.getId());
        }
    }

    @Async
    public void uploadGenreFilesAndUpdate(final Genre genre, final GenreFilesBufferDto genreFilesBufferDto) {
        Logger.debug("Start async image loading for a genre: " + genre.getId());
        try {
            if (genreFilesBufferDto.getImage() == null) {
                return;
            }

            if (genre.getImageUrl() != null && !genre.getImageUrl().isEmpty()) {
                amazonS3Service.deleteFromS3(genre.getImageUrl());
            }

            String imageUrl = amazonS3Service.uploadAndGenerateKey(genreFilesBufferDto.getImage(), genreRootPathPrefix);

            genre.setImageUrl(imageUrl);
            genreService.update(genre);

            Logger.info("S3 files have been successfully uploaded and genre has been updated: " + genre.getId());
        } catch (Exception e) {
            Logger.error("Error when loading image async in S3: " + e.getMessage());
        }
    }

    @Async
    public void uploadUserFilesAndUpdate(final AppUser user, final UserFilesBufferDto userFilesBufferDto) {
        Logger.debug("Start async avatar loading for a user: " + user.getId());
        try {
            if (userFilesBufferDto.getAvatar() == null) {
                return;
            }

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                amazonS3Service.deleteFromS3(user.getAvatar());
            }

            String avatarUrl = amazonS3Service.uploadAndGenerateKey(userFilesBufferDto.getAvatar(), avatarRootPathPrefix);

            user.setAvatar(avatarUrl);
            user.setUpdatedAt(LocalDateTime.now());
            appUserService.update(user);

            Logger.info("S3 files have been successfully uploaded and user has been updated: " + user.getId());
        } catch (Exception e) {
            Logger.error("Error when loading avatar async in S3: " + e.getMessage());
        }
    }
}
