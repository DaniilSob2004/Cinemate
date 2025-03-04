package com.example.cinemate.service.redis.token;

import com.example.cinemate.service.auth.jwt.JwtTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenRedisStorage extends AbstractTokenRedisStorage {

    public AccessTokenRedisStorage(RedisTemplate<String, String> redisAccessTokenTemplate, JwtTokenService jwtTokenService) {
        super(redisAccessTokenTemplate, jwtTokenService);
    }
}
