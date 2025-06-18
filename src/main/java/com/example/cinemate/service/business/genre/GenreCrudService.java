package com.example.cinemate.service.business.genre;

import com.example.cinemate.dto.genre.*;
import com.example.cinemate.dto.genre.file.GenreFilesBufferDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.mapper.genre.GenreMapper;
import com.example.cinemate.model.db.Genre;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.redis.GenresTestStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class GenreCrudService {

    @Value("${amazon_s3.genre_root_path_prefix}")
    private String genreRootPathPrefix;

    private final AmazonS3Service amazonS3Service;
    private final GenreService genreService;
    private final GenresTestStorage genresTestStorage;
    private final AccessJwtTokenService accessJwtTokenService;
    private final GenreMapper genreMapper;

    public GenreCrudService(AmazonS3Service amazonS3Service, GenreService genreService, GenresTestStorage genresTestStorage, AccessJwtTokenService accessJwtTokenService, GenreMapper genreMapper) {
        this.amazonS3Service = amazonS3Service;
        this.genreService = genreService;
        this.genresTestStorage = genresTestStorage;
        this.accessJwtTokenService = accessJwtTokenService;
        this.genreMapper = genreMapper;
    }

    public List<GenreDto> getAll() {
        return genreService.findAll().stream()
                .map(genreMapper::toGenreDto)
                .toList();
    }

    @Transactional
    public Genre add(final GenreDto genreDto) {
        genreDto.setName(genreDto.getName().toLowerCase());

        genreService.findByName(genreDto.getName())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Genre '" + genreDto.getName() + "' already exists");
                });

        return genreService.save(genreMapper.toGenre(genreDto));
    }

    @Async
    public void uploadFilesAndUpdate(final Genre genre, final GenreFilesBufferDto genreFilesBufferDto) {
        // загружаем картинку в s3
        if (genreFilesBufferDto.getImage() == null) {
            return;
        }
        String imageUrl = amazonS3Service.uploadAndGenerateKey(genreFilesBufferDto.getImage(), genreRootPathPrefix);

        // сохранение жанра
        genre.setImageUrl(imageUrl);

        genreService.update(genre);

        Logger.info("S3 files have been successfully uploaded and genre has been updated: " + genre.getId());
    }

    public void addGenresTest(final GenreRecTestDto genreRecTestDto, final HttpServletRequest request) {
        var genreIds = genreRecTestDto.getGenreIds();
        if (genreIds.isEmpty()) {
            return;
        }

        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);
        genresTestStorage.addToStorage(appUserJwtDto.getId().toString(), genreIds);
    }

    public List<Integer> getGenreTest(final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);
        return genresTestStorage.getGenreIds(appUserJwtDto.getId().toString()).orElseGet(List::of);
    }
}
