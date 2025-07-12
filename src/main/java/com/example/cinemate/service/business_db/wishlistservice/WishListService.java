package com.example.cinemate.service.business_db.wishlistservice;

import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.model.db.WishList;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishListService {
    void save(WishList wishList);
    void saveWishListsList(List<WishList> wishLists);
    void update(WishList wishList);
    void delete(WishList wishList);
    List<WishList> findAll();
    void deleteAll();

    boolean existsByUserIdAndContentId(Integer userId, Integer contentId);
    Page<WishList> getWishlists(WishlistParamsDto wishlistParamsDto);
    List<WishList> findByUserId(Integer userId);
}
