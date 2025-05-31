package com.example.cinemate.service.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class GenresTestStorage extends AbstractRedisRepository<List<Integer>> {

    @Value("${redis_data.genres_test_key_prefix}")
    private String keyPrefix;

    @Value("${redis_data.genres_test_expiration_time}")
    private long expirationTime;

    public GenresTestStorage(RedisTemplate<String, List<Integer>> redisGenresTestTemplate) {
        super(redisGenresTestTemplate);
    }

    public void addToStorage(final String userId, final List<Integer> genresIds) {
        String key = keyPrefix + userId;
        this.save(key, genresIds, expirationTime, TimeUnit.HOURS);
    }

    public Optional<List<Integer>> getGenreIds(final String userId) {
        String key = keyPrefix + userId;
        return Optional.ofNullable(this.get(key));
    }

    public void remove(final String userId) {
        String key = keyPrefix + userId;
        this.delete(key);
    }
}
