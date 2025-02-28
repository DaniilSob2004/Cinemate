package com.example.cinemate.service.redis;

import com.example.cinemate.utils.DateTimeUtil;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenRedisStorage extends AbstractRedisRepository<String> {

    @Value("${redis_data.refresh_token_key_prefix}")
    private String refreshTokenKeyPrefix;

    private final JwtTokenService jwtTokenService;

    public RefreshTokenRedisStorage(RedisTemplate<String, String> redisRefreshTokenTemplate, JwtTokenService jwtTokenService) {
        super(redisRefreshTokenTemplate);
        this.jwtTokenService = jwtTokenService;
    }

    public void add(final String token, final String userId) {
        Date expirationDate = jwtTokenService.getExpirationDateFromToken(token);
        long ttl = DateTimeUtil.calculateTtl(expirationDate);

        if (ttl > 0 && !this.exists(token)) {
            String key = refreshTokenKeyPrefix + token;
            this.save(key, userId, ttl, TimeUnit.SECONDS);
        }
    }

    public Optional<String> getUserId(final String token) {
        String key = refreshTokenKeyPrefix + token;
        String userId = this.get(key);
        return Optional.ofNullable(userId);
    }

    public void remove(final String token) {
        String key = refreshTokenKeyPrefix + token;
        this.delete(key);
    }

    public boolean isHave(final String token) {
        String key = refreshTokenKeyPrefix + token;
        return this.exists(key);
    }
}
