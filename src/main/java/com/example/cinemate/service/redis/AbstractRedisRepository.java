package com.example.cinemate.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AbstractRedisRepository<T> {

    protected final RedisTemplate<String, T> redisTemplate;

    protected void save(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    protected T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void delete(String key) {
        redisTemplate.delete(key);
    }

    protected boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
