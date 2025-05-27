package com.example.cinemate.dao.wishlist;

import com.example.cinemate.model.db.WishList;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends PagingAndSortingRepository<WishList, Integer>, JpaSpecificationExecutor<WishList> {
    List<WishList> findWishListsByUserId(Integer userId);
}
