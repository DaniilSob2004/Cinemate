package com.example.cinemate.service.business.content;

import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.content.ContentNotFoundException;
import com.example.cinemate.mapper.ContentMapper;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.model.db.ContentActor;
import com.example.cinemate.model.db.ContentGenre;
import com.example.cinemate.model.db.ContentWarning;
import com.example.cinemate.service.business_db.actorservice.ActorService;
import com.example.cinemate.service.business_db.contentactorservice.ContentActorService;
import com.example.cinemate.service.business_db.contentgenreservice.ContentGenreService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import com.example.cinemate.service.business_db.contentwarningservice.ContentWarningService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.business_db.warningservice.WarningService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentAdminCrudService {

    private final ContentService contentService;
    private final ContentTypeService contentTypeService;
    private final GenreService genreService;
    private final ContentGenreService contentGenreService;
    private final ActorService actorService;
    private final ContentActorService contentActorService;
    private final WarningService warningService;
    private final ContentWarningService contentWarningService;
    private final ContentMapper contentMapper;

    public ContentAdminCrudService(ContentService contentService, ContentTypeService contentTypeService, GenreService genreService, ContentGenreService contentGenreService, ActorService actorService, ContentActorService contentActorService, WarningService warningService, ContentWarningService contentWarningService, ContentMapper contentMapper) {
        this.contentService = contentService;
        this.contentTypeService = contentTypeService;
        this.genreService = genreService;
        this.contentGenreService = contentGenreService;
        this.actorService = actorService;
        this.contentActorService = contentActorService;
        this.warningService = warningService;
        this.contentWarningService = contentWarningService;
        this.contentMapper = contentMapper;
    }

    public void add(final ContentFullAdminDto contentFullAdminDto) {
        // есть ли такой контент
        contentService.findByName(contentFullAdminDto.getName().toLowerCase())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Content '" + contentFullAdminDto.getName() + "' already exists");
                });

        // есть ли такой тип контента
        var contentType = contentTypeService.findByName(contentFullAdminDto.getContentType().toLowerCase()).orElse(null);
        if (contentType == null) {
            throw new ContentNotFoundException("Content type '" + contentFullAdminDto.getContentType() + "' not found");
        }

        // сохранение контента
        contentFullAdminDto.setCreatedAt(LocalDateTime.now());
        contentFullAdminDto.setUpdatedAt(LocalDateTime.now());
        Content content = contentMapper.toContent(contentFullAdminDto);
        content.setContentType(contentType);
        Content newContent = contentService.save(content);

        // добавление жанров
        this.addContentGenres(newContent, contentFullAdminDto.getGenres());

        // добавление актеров
        this.addContentActors(newContent, contentFullAdminDto.getActors());

        // добавление warnings
        this.addContentWarnings(newContent, contentFullAdminDto.getWarnings());
    }

    private void addContentGenres(final Content newContent, final List<Integer> idGenres) {
        List<ContentGenre> newContentGenres = new ArrayList<>();
        for (int idGenre : idGenres) {
            var genre = genreService.findById(idGenre).orElse(null);
            if (genre == null) {
                continue;
            }
            newContentGenres.add(new ContentGenre(
                    null,
                    newContent,
                    genre
            ));
        }
        contentGenreService.saveContentGenresList(newContentGenres);
    }

    private void addContentActors(final Content newContent, final List<Integer> idActors) {
        List<ContentActor> newContentActors = new ArrayList<>();
        for (int idActor : idActors) {
            var actor = actorService.findById(idActor).orElse(null);
            if (actor == null) {
                continue;
            }
            newContentActors.add(new ContentActor(
                    null,
                    newContent,
                    actor
            ));
        }
        contentActorService.saveContentActorsList(newContentActors);
    }

    private void addContentWarnings(final Content newContent, final List<Integer> idWarnings) {
        List<ContentWarning> newContentWarnings = new ArrayList<>();
        for (int idWarning : idWarnings) {
            var warning = warningService.findById(idWarning).orElse(null);
            if (warning == null) {
                continue;
            }
            newContentWarnings.add(new ContentWarning(
                    null,
                    newContent,
                    warning
            ));
        }
        contentWarningService.saveContentWarningsList(newContentWarnings);
    }
}
