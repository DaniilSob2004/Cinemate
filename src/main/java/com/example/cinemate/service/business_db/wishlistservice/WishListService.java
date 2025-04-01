package com.example.cinemate.service.business_db.wishlistservice;

import com.example.cinemate.model.db.WishList;

import java.util.List;

public interface WishListService {
    void save(WishList wishList);
    void saveWishListsList(List<WishList> wishLists);
    void update(WishList wishList);
    void delete(WishList wishList);
    List<WishList> findAll();
    void deleteAll();
}
