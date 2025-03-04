package com.example.cinemate.service.redis.token;

import com.example.cinemate.service.auth.jwt.JwtTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenRedisStorage extends AbstractTokenRedisStorage {

    public RefreshTokenRedisStorage(RedisTemplate<String, String> redisRefreshTokenTemplate, JwtTokenService jwtTokenService) {
        super(redisRefreshTokenTemplate, jwtTokenService);
    }
}
