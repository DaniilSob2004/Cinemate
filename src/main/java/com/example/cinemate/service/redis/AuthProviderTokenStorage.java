package com.example.cinemate.service.redis;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthProviderTokenStorage extends AbstractRedisRepository<ResponseAuthDto> {

    @Value("${redis_data.oauth_success_token_key_prefix}")
    private String keyPrefix;

    @Value("${redis_data.oauth_success_token_expiration_time}")
    private int expirationTime;

    public AuthProviderTokenStorage(RedisTemplate<String, ResponseAuthDto> redisSuccessAuthProviderTokenTemplate) {
        super(redisSuccessAuthProviderTokenTemplate);
    }

    public void addToStorage(final String stateId, final ResponseAuthDto responseAuthDto) {
        String key = keyPrefix + stateId;
        this.save(key, responseAuthDto, expirationTime, TimeUnit.MINUTES);
    }

    public Optional<ResponseAuthDto> getResponseAuthDto(final String stateId) {
        String key = keyPrefix + stateId;
        return Optional.ofNullable(this.get(key));
    }

    public void remove(final String stateId) {
        String key = keyPrefix + stateId;
        this.delete(key);
    }
}
