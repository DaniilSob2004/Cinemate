package com.example.cinemate.mapper;

import com.example.cinemate.dto.wishlist.WishlistDto;
import com.example.cinemate.mapper.common.ModelProxyLinkMapper;
import com.example.cinemate.model.db.WishList;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WishlistMapper {

    private final AmazonS3Service amazonS3Service;
    private final ModelProxyLinkMapper modelProxyLinkMapper;

    public WishlistMapper(AmazonS3Service amazonS3Service, ModelProxyLinkMapper modelProxyLinkMapper) {
        this.amazonS3Service = amazonS3Service;
        this.modelProxyLinkMapper = modelProxyLinkMapper;
    }

    public WishlistDto toWishListDto(final WishList wishlist) {
        var content = wishlist.getContent();
        return new WishlistDto(
                content.getId(),
                content.getName(),
                content.getContentType().getName(),
                amazonS3Service.getCloudFrontUrl(content.getPosterUrl()),
                content.getDurationMin(),
                content.getAgeRating(),
                DateTimeUtil.formatDateTime(wishlist.getCreatedAt())
        );
    }

    public WishList toWishlist(final int userId, final int contentId) {
        return new WishList(
                null,
                modelProxyLinkMapper.getAppUserById(userId),
                modelProxyLinkMapper.getContentById(contentId),
                LocalDateTime.now()
        );
    }
}
