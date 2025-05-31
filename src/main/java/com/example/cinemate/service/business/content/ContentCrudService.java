package com.example.cinemate.service.business.content;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.common.PaginationSearchParamsDto;
import com.example.cinemate.dto.content.*;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.exception.genre.GenresTestDataNotFoundException;
import com.example.cinemate.mapper.content.*;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.contentgenreservice.ContentGenreService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import com.example.cinemate.service.business_db.contentviewhistoryservice.ContentViewHistoryService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.business_db.wishlistservice.WishListService;
import com.example.cinemate.service.redis.GenresTestStorage;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class ContentCrudService {

    private final ContentService contentService;
    private final GenreService genreService;
    private final ContentTypeService contentTypeService;
    private final ContentGenreService contentGenreService;
    private final WishListService wishListService;
    private final ContentViewHistoryService contentViewHistoryService;
    private final GenresTestStorage genresTestStorage;
    private final ContentMapper contentMapper;
    private final ContentEnrichMapper contentEnrichMapper;
    private final AccessJwtTokenService accessJwtTokenService;

    public ContentCrudService(ContentService contentService, GenreService genreService, ContentTypeService contentTypeService, ContentGenreService contentGenreService, WishListService wishListService, ContentViewHistoryService contentViewHistoryService, GenresTestStorage genresTestStorage, ContentMapper contentMapper, ContentEnrichMapper contentEnrichMapper, AccessJwtTokenService accessJwtTokenService) {
        this.contentService = contentService;
        this.genreService = genreService;
        this.contentTypeService = contentTypeService;
        this.contentGenreService = contentGenreService;
        this.wishListService = wishListService;
        this.contentViewHistoryService = contentViewHistoryService;
        this.genresTestStorage = genresTestStorage;
        this.contentMapper = contentMapper;
        this.contentEnrichMapper = contentEnrichMapper;
        this.accessJwtTokenService = accessJwtTokenService;
    }

    public PagedResponse<ContentDto> getByRecommend(final ContentRecSearchParamsDto contentRecSearchParamsDto, final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);
        var userId = appUserJwtDto.getId();

        List<Integer> genreIds;
        List<Integer> contentTypeIds = null;

        // автоматический расчёт рекоммендации
        if (contentRecSearchParamsDto.getIsAuto()) {
            // список желаний
            var userWishList = wishListService.findByUserId(userId);

            // просмотренный контент
            var viewHistories = contentViewHistoryService.findByUserId(userId);

            // список genreIds по "WishList" и "ContentViewHistory"
            List<Integer> allContentIds = Stream.concat(
                    userWishList.stream().map(w -> w.getContent().getId()),
                    viewHistories.stream().map(vh -> vh.getContent().getId())
            ).distinct().toList();
            genreIds = contentGenreService.getGenresByContentIds(allContentIds).values().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .toList();

            // список contentTypeIds по "WishList" и "ContentViewHistory"
            contentTypeIds = Stream.concat(
                    userWishList.stream().map(w -> w.getContent().getContentType().getId()),
                    viewHistories.stream().map(vh -> vh.getContent().getContentType().getId())
            ).distinct().toList();
        }
        else {
            genreIds = genresTestStorage.getGenreIds(userId.toString())
                    .orElseThrow(() -> new GenresTestDataNotFoundException("No genres test data found for user " + userId));
        }

        contentRecSearchParamsDto.setGenreIds(genreIds);
        contentRecSearchParamsDto.setContentTypeIds(contentTypeIds);

        return this.getPagedContents(contentRecSearchParamsDto, contentService::getRecommendedContents);
    }

    public List<ContentDto> getRandom(final ContentRandomRequestDto contentRandomRequestDto) {
        long totalElements = contentService.getTotalContents();
        int maxPage = (int) (totalElements / contentRandomRequestDto.getCount());

        var contentSearchParamsDto = ContentSearchParamsDto.builder()
                .typeId(null)
                .genreId(null)
                .searchStr("")
                .isActive(true)
                .page(GenerateUtil.getRandomInteger(0, Math.max(1, maxPage)))
                .size(contentRandomRequestDto.getCount())
                .sortBy("id")
                .isAsc(GenerateUtil.getRandomBoolean())
                .build();

        Page<Content> pageContents = contentService.getContents(contentSearchParamsDto);
        var contentsDto = new ArrayList<>(this.mapContentToDtoWithDetails(pageContents));

        Collections.shuffle(contentsDto);

        return contentsDto;
    }

    public PagedResponse<ContentDto> getByGenre(final ContentSearchParamsDto contentSearchParamsDto) {
        genreService.findById(contentSearchParamsDto.getGenreId())
                .orElseThrow(() -> new ContentNotFoundException("Genre with id '" + contentSearchParamsDto.getGenreId() + "' not found"));

        return this.getPagedContents(contentSearchParamsDto, contentService::getContents);
    }

    public PagedResponse<ContentDto> getByType(final ContentSearchParamsDto contentSearchParamsDto) {
        contentTypeService.findById(contentSearchParamsDto.getTypeId())
                .orElseThrow(() -> new ContentNotFoundException("Content type with id '" + contentSearchParamsDto.getTypeId() + "' not found"));

        return this.getPagedContents(contentSearchParamsDto, contentService::getContents);
    }


    private <T extends PaginationSearchParamsDto> PagedResponse<ContentDto> getPagedContents(T dto, Function<T, Page<Content>> contentFetcher) {
        dto.setPage(dto.getPage() - 1);
        Page<Content> pageContents = contentFetcher.apply(dto);

        return PaginationUtil.getPagedResponse(
                this.mapContentToDtoWithDetails(pageContents),
                pageContents
        );
    }

    private List<ContentDto> mapContentToDtoWithDetails(final Page<Content> pageContents) {
        var contents = pageContents.getContent();
        var contentsDto = contents.stream().map(contentMapper::toContentDto).toList();
        contentEnrichMapper.enrichContentDto(contentsDto);
        return contentsDto;
    }
}
