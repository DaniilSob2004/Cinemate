package com.example.cinemate.service.business.wishlist;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.wishlist.WishlistDto;
import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.mapper.WishlistMapper;
import com.example.cinemate.model.db.WishList;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.wishlistservice.WishListService;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WishlistCrudService {

    private final WishListService wishListService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final WishlistMapper wishlistMapper;

    public WishlistCrudService(WishListService wishListService, AccessJwtTokenService accessJwtTokenService, WishlistMapper wishlistMapper) {
        this.wishListService = wishListService;
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
}
