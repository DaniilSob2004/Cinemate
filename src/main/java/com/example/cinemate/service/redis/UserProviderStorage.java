package com.example.cinemate.service.redis;

import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserProviderStorage extends AbstractRedisRepository<String> {

    @Value("${redis_data.user_provider_key_prefix}")
    private String keyPrefix;

    @Value("${redis_data.base_redis_expiration_time}")
    private long expirationTime;

    private final ExternalAuthService externalAuthService;

    public UserProviderStorage(RedisTemplate<String, String> redisUserProviderTemplate, ExternalAuthService externalAuthService) {
        super(redisUserProviderTemplate);
        this.externalAuthService = externalAuthService;
    }

    public void addToStorage(final String userId, final String provider) {
        // если объект уже в кэше
        if (provider == null || this.isExists(userId)) {
            return;
        }
        String key = keyPrefix + userId;
        this.save(key, provider, expirationTime, TimeUnit.SECONDS);
    }

    public String getProvider(final String userId) {
        String key = keyPrefix + userId;
        Optional<String> providerFromCache = Optional.ofNullable(this.get(key));

        // в кэше данных нет
        if (providerFromCache.isEmpty()) {
            String userProvider = externalAuthService.findByUserId(Integer.valueOf(userId))
                    .map(auth -> auth.getProvider().getName())
                    .orElse("");
            this.addToStorage(userId, userProvider);
            return userProvider;
        }

        return providerFromCache.get();
    }

    public Map<Integer, String> getProviders(List<Integer> userIds) {
        List<String> keys = userIds.stream()
                .map(id -> keyPrefix + id)
                .toList();

        // одним запросом получаем значения
        List<String> providerValues = this.redisTemplate.opsForValue().multiGet(keys);

        Map<Integer, String> result = new HashMap<>();
        if (providerValues != null) {
            for (int i = 0; i < userIds.size(); i++) {
                Integer userId = userIds.get(i);
                String provider = providerValues.get(i);
                if (provider != null) {
                    result.put(userId, provider);
                }
                else {
                    String userProvider = externalAuthService.findByUserId(userId)
                            .map(auth -> auth.getProvider().getName())
                            .orElse("");
                    this.addToStorage(userId.toString(), userProvider);
                    result.put(userId, userProvider);
                }
            }
        }

        return result;
    }

    public boolean isExists(final String userId) {
        String key = keyPrefix + userId;
        return this.exists(key);
    }
}
