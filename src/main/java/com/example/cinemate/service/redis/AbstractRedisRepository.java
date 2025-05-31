package com.example.cinemate.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class AbstractRedisRepository<T> {

    protected final RedisTemplate<String, T> redisTemplate;

    protected void save(String key, T value, long timeout, TimeUnit unit) {
        if (timeout <= 0) {
            redisTemplate.opsForValue().set(key, value);  // бессрочно
        }
        else {
            redisTemplate.opsForValue().set(key, value, timeout, unit);  // с TTL
        }
    }

    protected void saveToSet(String key, T value, long ttl, TimeUnit unit) {
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, ttl, unit);
    }

    protected T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected Set<T> getFromSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    protected void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(String[] keys) {
        if (keys != null && keys.length > 0) {
            redisTemplate.delete(Arrays.asList(keys));
        }
    }

    protected void deleteToSet(String key, T value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    protected boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    protected int getSizeToSet(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size.intValue() : 0;
    }
}
