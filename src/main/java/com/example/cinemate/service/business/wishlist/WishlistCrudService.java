package com.example.cinemate.service.business.wishlist;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.wishlist.WishListAddDto;
import com.example.cinemate.dto.wishlist.WishlistDto;
import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.mapper.WishlistMapper;
import com.example.cinemate.model.db.WishList;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.contentservice.ContentService;
import com.example.cinemate.service.business_db.wishlistservice.WishListService;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WishlistCrudService {

    private final WishListService wishListService;
    private final AppUserService appUserService;
    private final ContentService contentService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final WishlistMapper wishlistMapper;

    public WishlistCrudService(WishListService wishListService, AppUserService appUserService, ContentService contentService, AccessJwtTokenService accessJwtTokenService, WishlistMapper wishlistMapper) {
        this.wishListService = wishListService;
        this.appUserService = appUserService;
        this.contentService = contentService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.wishlistMapper = wishlistMapper;
    }

    public PagedResponse<WishlistDto> getByUserId(final WishlistParamsDto wishlistParamsDto, final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        wishlistParamsDto.setPage(wishlistParamsDto.getPage() - 1);
        wishlistParamsDto.setUserId(appUserJwtDto.getId());

        Page<WishList> pageWishlists = wishListService.getWishlists(wishlistParamsDto);

        var wishListsDto = pageWishlists.get()
                .map(wishlistMapper::toWishListDto)
                .toList();

        return PaginationUtil.getPagedResponse(
                wishListsDto,
                pageWishlists
        );
    }

    public void add(final WishListAddDto wishListAddDto, final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        if (!appUserService.existsById(appUserJwtDto.getId())) {
            throw new UserNotFoundException("User with id " + appUserJwtDto.getId() + " not found");
        }

        if (!contentService.existsById(wishListAddDto.getContentId())) {
            throw new ContentNotFoundException("Content with id " + wishListAddDto.getContentId() + " not found");
        }

        if (wishListService.existsByUserIdAndContentId(appUserJwtDto.getId(), wishListAddDto.getContentId())) {
            throw new ContentAlreadyExists("Content already exists in wishlist");
        }

        var addWishList = wishlistMapper.toWishlist(appUserJwtDto.getId(), wishListAddDto.getContentId());
        wishListService.save(addWishList);
    }
}
