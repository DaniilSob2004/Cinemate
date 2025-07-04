package com.example.cinemate.service.business.genre;

import com.example.cinemate.dto.genre.*;
import com.example.cinemate.dto.genre.file.GenreFilesDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.mapper.genre.GenreFileMapper;
import com.example.cinemate.mapper.genre.GenreMapper;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business.common.UploadFilesAsyncService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.redis.GenresTestStorage;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class GenreCrudService {

    private final UploadFilesAsyncService uploadFilesAsyncService;
    private final GenreService genreService;
    private final GenresTestStorage genresTestStorage;
    private final AccessJwtTokenService accessJwtTokenService;
    private final GenreMapper genreMapper;
    private final GenreFileMapper genreFileMapper;

    public GenreCrudService(UploadFilesAsyncService uploadFilesAsyncService, GenreService genreService, GenresTestStorage genresTestStorage, AccessJwtTokenService accessJwtTokenService, GenreMapper genreMapper, GenreFileMapper genreFileMapper) {
        this.uploadFilesAsyncService = uploadFilesAsyncService;
        this.genreService = genreService;
        this.genresTestStorage = genresTestStorage;
        this.accessJwtTokenService = accessJwtTokenService;
        this.genreMapper = genreMapper;
        this.genreFileMapper = genreFileMapper;
    }

    public List<GenreDto> getAll() {
        return genreService.findAll().stream()
                .map(genreMapper::toGenreDto)
                .toList();
    }

    @Transactional
    public void add(final GenreDto genreDto, final GenreFilesDto genreFilesDto) {
        genreDto.setName(genreDto.getName().toLowerCase());

        genreService.findByName(genreDto.getName())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Genre '" + genreDto.getName() + "' already exists");
                });

        var savedGenre = genreService.save(genreMapper.toGenre(genreDto));

        // в отдельном потоке загружаем картинку
        var genreFilesBufferDto = genreFileMapper.toGenreFilesBufferDto(genreFilesDto);
        uploadFilesAsyncService.uploadGenreFilesAndUpdate(savedGenre, genreFilesBufferDto);
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
