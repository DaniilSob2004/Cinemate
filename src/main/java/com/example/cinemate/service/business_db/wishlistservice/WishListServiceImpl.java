package com.example.cinemate.service.business_db.wishlistservice;

import com.example.cinemate.dao.wishlist.WishListRepository;
import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.model.db.WishList;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
        return (List<WishList>) wishListRepository.findAll();
    }

    @Override
    public void deleteAll() {
        wishListRepository.deleteAll();
    }


    @Override
    public boolean existsByUserIdAndContentId(Integer userId, Integer contentId) {
        return wishListRepository.existsWishListByUserIdAndContentId(userId, contentId);
    }

    @Override
    public Page<WishList> getWishlists(WishlistParamsDto wishlistParamsDto) {
        Pageable pageable = PaginationUtil.getPageable(wishlistParamsDto);
        Specification<WishList> specWishlist = Specification.where(this.searchSpecification(wishlistParamsDto));
        return wishListRepository.findAll(specWishlist, pageable);
    }

    private Specification<WishList> searchSpecification(final WishlistParamsDto wishlistParamsDto) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // фильтр по id пользователя
            if (wishlistParamsDto.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), wishlistParamsDto.getUserId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<WishList> findByUserId(Integer userId) {
        return wishListRepository.findWishListsByUserId(userId);
    }
}
