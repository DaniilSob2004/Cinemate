package com.example.cinemate.service.business_db.wishlistservice;

import com.example.cinemate.dao.wishlist.WishListRepository;
import com.example.cinemate.model.db.WishList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public void save(WishList wishList) {
        wishListRepository.save(wishList);
    }

    @Override
    public void saveWishListsList(List<WishList> wishLists) {
        wishListRepository.saveAll(wishLists);
    }

    @Override
    public void update(WishList wishList) {
        wishListRepository.save(wishList);
    }

    @Override
    public void delete(WishList wishList) {
        wishListRepository.delete(wishList);
    }

    @Override
    public List<WishList> findAll() {
        return wishListRepository.findAll();
    }

    @Override
    public void deleteAll() {
        wishListRepository.deleteAll();
    }

    @Override
    public List<WishList> findByUserId(Integer userId) {
        return wishListRepository.findWishListsByUserId(userId);
    }
}
