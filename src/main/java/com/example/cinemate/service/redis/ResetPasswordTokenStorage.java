package com.example.cinemate.service.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ResetPasswordTokenStorage extends AbstractRedisRepository<String> {

    @Value("${redis_data.reset_token_key_prefix}")
    private String keyPrefix;

    @Value("${redis_data.reset_token_expiration_time}")
    private long expirationTime;

    public ResetPasswordTokenStorage(RedisTemplate<String, String> redisResetPasswordTokenTemplate) {
        super(redisResetPasswordTokenTemplate);
    }

    public void addToStorage(final String token, final String userId) {
        String key = keyPrefix + token;
        this.save(key, userId, expirationTime, TimeUnit.SECONDS);
    }

    public void remove(final String token) {
        String key = keyPrefix + token;
        this.delete(key);
    }
}
