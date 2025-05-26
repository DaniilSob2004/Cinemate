package com.example.cinemate.dao.wishlist;

import com.example.cinemate.model.db.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findWishListsByUserId(Integer userId);
}
