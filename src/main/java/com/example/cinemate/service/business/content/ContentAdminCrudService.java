package com.example.cinemate.service.business.content;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.content.*;
import com.example.cinemate.dto.content.files.ContentFilesBufferDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.mapper.content.*;
import com.example.cinemate.model.db.*;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.service.business_db.actorservice.ActorService;
import com.example.cinemate.service.business_db.contentactorservice.ContentActorService;
import com.example.cinemate.service.business_db.contentgenreservice.ContentGenreService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import com.example.cinemate.service.business_db.contentwarningservice.ContentWarningService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.business_db.warningservice.WarningService;
import com.example.cinemate.utils.DateTimeUtil;
import com.example.cinemate.utils.DiffUtil;
import com.example.cinemate.utils.PaginationUtil;
import com.example.cinemate.validate.common.CommonDataValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContentAdminCrudService {

    @Value("${amazon_s3.poster_root_path_prefix}")
    private String posterRootPathPrefix;

    @Value("${amazon_s3.trailer_root_path_prefix}")
    private String trailerRootPathPrefix;

    private final AmazonS3Service amazonS3Service;
    private final ContentService contentService;
    private final ContentTypeService contentTypeService;
    private final GenreService genreService;
    private final ContentGenreService contentGenreService;
    private final ActorService actorService;
    private final ContentActorService contentActorService;
    private final WarningService warningService;
    private final ContentWarningService contentWarningService;
    private final ContentMapper contentMapper;
    private final ContentEnrichMapper contentEnrichMapper;
    private final CommonDataValidate commonDataValidate;

    public ContentAdminCrudService(AmazonS3Service amazonS3Service, ContentService contentService, ContentTypeService contentTypeService, GenreService genreService, ContentGenreService contentGenreService, ActorService actorService, ContentActorService contentActorService, WarningService warningService, ContentWarningService contentWarningService, ContentMapper contentMapper, ContentEnrichMapper contentEnrichMapper, CommonDataValidate commonDataValidate) {
        this.amazonS3Service = amazonS3Service;
        this.contentService = contentService;
        this.contentTypeService = contentTypeService;
        this.genreService = genreService;
        this.contentGenreService = contentGenreService;
        this.actorService = actorService;
        this.contentActorService = contentActorService;
        this.warningService = warningService;
        this.contentWarningService = contentWarningService;
        this.contentMapper = contentMapper;
        this.contentEnrichMapper = contentEnrichMapper;
        this.commonDataValidate = commonDataValidate;
    }

    public PagedResponse<ContentListAdminDto> getListContents(final ContentSearchParamsDto contentSearchParamsDto) {
        String validSortBy = commonDataValidate.getIsFieldExists(
                contentSearchParamsDto.getSortBy(),
                Content.class.getDeclaredFields()
        );

        contentSearchParamsDto.setPage(contentSearchParamsDto.getPage() - 1);
        contentSearchParamsDto.setSortBy(validSortBy);

        Page<Content> pageContents = contentService.getContents(contentSearchParamsDto);

        List<ContentListAdminDto> contentsDto = pageContents.get()
                .map(contentMapper::toContentListAdminDto)
                .toList();

        return PaginationUtil.getPagedResponse(contentsDto, pageContents);
    }

    public ContentFullAdminDto getById(final Integer id) {
        Content content = contentService.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Content with id '" + id + "' not found"));

        var contentFullAdminDto = contentMapper.toContentFullAdminDto(content);
        contentEnrichMapper.enrichContentDto(List.of(contentFullAdminDto));

        return contentFullAdminDto;
    }


    public Content addInitial(final ContentFullAdminDto contentFullAdminDto, final ContentFilesBufferDto contentFilesBufferDto) {
        // есть ли такой контент
        contentService.findByName(contentFullAdminDto.getName().toLowerCase())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Content '" + contentFullAdminDto.getName() + "' already exists");
                });

        // есть ли такой тип контента
        String contentTypeName = contentFullAdminDto.getContentType();
        var contentType = contentTypeService.findByName(contentTypeName.toLowerCase())
                .orElseThrow(() -> new ContentNotFoundException("Content type '" + contentTypeName + "' not found"));

        // загружаем постер в s3
        String posterKey = amazonS3Service.uploadAndGenerateKey(contentFilesBufferDto.getPoster(), posterRootPathPrefix);

        // сохранение контента
        contentFullAdminDto.setPosterUrl(posterKey);
        contentFullAdminDto.setCreatedAt(LocalDateTime.now());
        contentFullAdminDto.setUpdatedAt(LocalDateTime.now());
        Content content = contentMapper.toContent(contentFullAdminDto);
        content.setContentType(contentType);
        Content newContent = contentService.save(content);

        // добавление жанров, актеров, warnings
        this.addContentGenres(newContent, contentFullAdminDto.getGenres());
        this.addContentActors(newContent, contentFullAdminDto.getActors());
        this.addContentWarnings(newContent, contentFullAdminDto.getWarnings());

        return newContent;
    }

    @Async
    public void uploadFilesAndUpdate(final Content content, final ContentFilesBufferDto contentFilesBufferDto) {
        // загружаем трейлер и видео в s3
        String trailerKey = amazonS3Service.uploadAndGenerateKey(contentFilesBufferDto.getTrailer(), trailerRootPathPrefix);
        String videoKey = amazonS3Service.uploadAndGenerateKey(contentFilesBufferDto.getVideo(), trailerRootPathPrefix);

        // сохранение контента
        content.setTrailerUrl(trailerKey);
        content.setVideoUrl(videoKey);
        content.setUpdatedAt(LocalDateTime.now());

        contentService.save(content);

        Logger.info("S3 files have been successfully uploaded and content has been updated: " + content.getId());
    }


    @Transactional
    public void updateById(final Integer id, final ContentFullAdminDto contentFullAdminDto) {
        // есть ли такой контент
        var content = contentService.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Content with id '" + id + "' not found"));

        // есть ли такой тип контента
        String contentTypeName = contentFullAdminDto.getContentType().toLowerCase();
        var contentType = contentTypeService.findByName(contentTypeName)
                .orElseThrow(() -> new ContentNotFoundException("Content type '" + contentFullAdminDto.getContentType() + "' not found"));

        // если ContentType разные
        if (!content.getContentType().getName().equals(contentType.getName())) {
            content.setContentType(contentType);
        }

        // если releaseDate больше текущей даты, то VideoUrl ставим пустым (фильм ещё не вышел)
        if (DateTimeUtil.isDateAfterNow(contentFullAdminDto.getReleaseDate())) {
            content.setVideoUrl("");
        }

        // обновление данных
        this.updateContentActors(content, contentFullAdminDto.getActors());
        this.updateContentWarnings(content, contentFullAdminDto.getWarnings());
        this.updateContentGenres(content, contentFullAdminDto.getGenres());
        this.updateContent(content, contentFullAdminDto);
    }

    @Transactional
    public void delete(final Integer id) {
        contentService.findById(id).ifPresentOrElse(
                content -> {
                    content.setActive(false);
                    contentService.save(content);
                },
                () -> {
                    throw new ContentNotFoundException("Content with id '" + id + "' not found");
                }
        );
    }


    private void updateContentGenres(final Content content, final List<Integer> listUpdatedGenreIds) {
        // получение id жанров для добавления и удаления
        var idsToAddAndRemove = DiffUtil.calculateDiffIds(
                contentGenreService.getIdGenres(content.getId()),
                listUpdatedGenreIds
        );

        // обновление в БД
        this.addContentGenres(content, new ArrayList<>(idsToAddAndRemove.getFirst()));
        this.deleteContentGenres(content.getId(), new ArrayList<>(idsToAddAndRemove.getSecond()));
    }

    private void updateContentActors(final Content content, final List<Integer> listUpdatedActorIds) {
        // получение id актеров для добавления и удаления
        var idsToAddAndRemove = DiffUtil.calculateDiffIds(
                contentActorService.getIdActors(content.getId()),
                listUpdatedActorIds
        );

        // обновление в БД
        this.addContentActors(content, new ArrayList<>(idsToAddAndRemove.getFirst()));
        this.deleteContentActors(content.getId(), new ArrayList<>(idsToAddAndRemove.getSecond()));
    }

    private void updateContentWarnings(final Content content, final List<Integer> listUpdateWarningIds) {
        // получение id warnings для добавления и удаления
        var idsToAddAndRemove = DiffUtil.calculateDiffIds(
                contentWarningService.getIdWarnings(content.getId()),
                listUpdateWarningIds
        );

        // обновление в БД
        this.addContentWarnings(content, new ArrayList<>(idsToAddAndRemove.getFirst()));
        this.deleteContentWarnings(content.getId(), new ArrayList<>(idsToAddAndRemove.getSecond()));
    }

    private void updateContent(final Content content, final ContentFullAdminDto contentFullAdminDto) {
        Content updateContent = contentMapper.toContent(contentFullAdminDto);
        updateContent.setId(content.getId());
        updateContent.setContentType(content.getContentType());
        updateContent.setCreatedAt(content.getCreatedAt());
        updateContent.setUpdatedAt(LocalDateTime.now());
        contentService.update(updateContent);
    }


    private void addContentGenres(final Content newContent, final List<Integer> idGenres) {
        List<ContentGenre> newContentGenres = new ArrayList<>();
        for (int idGenre : idGenres) {
            genreService.findById(idGenre).ifPresent(genre ->
                    newContentGenres.add(new ContentGenre(null, newContent, genre)));
        }
        contentGenreService.saveContentGenresList(newContentGenres);
    }

    private void addContentActors(final Content newContent, final List<Integer> idActors) {
        List<ContentActor> newContentActors = new ArrayList<>();
        for (int idActor : idActors) {
            actorService.findById(idActor).ifPresent(actor ->
                newContentActors.add(new ContentActor(null, newContent, actor)));
        }
        contentActorService.saveContentActorsList(newContentActors);
    }

    private void addContentWarnings(final Content newContent, final List<Integer> idWarnings) {
        List<ContentWarning> newContentWarnings = new ArrayList<>();
        for (int idWarn : idWarnings) {
            warningService.findById(idWarn).ifPresent(warning ->
                newContentWarnings.add(new ContentWarning(null, newContent, warning)));
        }
        contentWarningService.saveContentWarningsList(newContentWarnings);
    }


    private void deleteContentGenres(final Integer idContent, final List<Integer> idGenres) {
        for (var idGenre : idGenres) {
            contentGenreService.deleteByContentIdAndGenreId(idContent, idGenre);
        }
    }

    private void deleteContentActors(final Integer idContent, final List<Integer> idActors) {
        for (var idActor : idActors) {
            contentActorService.deleteByContentIdAndActorId(idContent, idActor);
        }
    }

    private void deleteContentWarnings(final Integer idContent, final List<Integer> idWarnings) {
        for (var idWarn : idWarnings) {
            contentWarningService.deleteByContentIdAndWarningId(idContent, idWarn);
        }
    }
}
