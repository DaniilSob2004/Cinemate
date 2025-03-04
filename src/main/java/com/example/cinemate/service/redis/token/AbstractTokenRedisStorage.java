package com.example.cinemate.service.redis.token;

import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.redis.AbstractRedisRepository;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.tinylog.Logger;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AbstractTokenRedisStorage extends AbstractRedisRepository<String> {

    @Value("${redis_data.token_key_prefix}")
    private String tokenKeyPrefix;

    @Value("${redis_data.token_user_id_key_prefix}")
    private String tokenUserKeyPrefix;

    private final JwtTokenService jwtTokenService;

    public AbstractTokenRedisStorage(RedisTemplate<String, String> redisTemplate, JwtTokenService jwtTokenService) {
        super(redisTemplate);
        this.jwtTokenService = jwtTokenService;
    }

    public void add(final String token, final String userId) {
        Date expirationDate = jwtTokenService.getExpirationDateFromToken(token);
        long ttl = DateTimeUtil.calculateTtl(expirationDate);

        String tokenKey = tokenKeyPrefix + token;
        if (ttl > 0 && !this.exists(tokenKey)) {
            String userKey = tokenUserKeyPrefix + userId;

            // удаление истёкших токенов из множества
            deleteExpiredTokensFromSet(userKey);

            this.save(tokenKey, userId, ttl, TimeUnit.SECONDS);
            this.saveToSet(userKey, token, ttl, TimeUnit.SECONDS);
        }

        Logger.info("SIZE TO SET: " + this.getSizeToSet(tokenUserKeyPrefix + userId));
    }

    public void removeByToken(final String token) {
        String tokenKey = tokenKeyPrefix + token;
        String userId = this.get(tokenKey);

        if (userId != null) {
            String userKey = tokenUserKeyPrefix + userId;
            this.deleteToSet(userKey, token);

            // удаление истёкших токенов из множества
            deleteExpiredTokensFromSet(userKey);
        }

        Logger.info("SIZE TO SET: " + this.getSizeToSet(tokenUserKeyPrefix + userId));

        this.delete(tokenKey);
    }

    public void removeByUserId(final String userId) {
        String userKey = tokenUserKeyPrefix + userId;
        Set<String> tokens = this.getFromSet(userKey);

        if (tokens != null && !tokens.isEmpty()) {
            List<String> keysToDelete = tokens.stream()
                    .map(token -> tokenKeyPrefix + token)
                    .collect(Collectors.toList());
            keysToDelete.add(userKey);

            this.delete(keysToDelete.toArray(new String[0]));  // удаляем все ключи
        }
        else {
            this.delete(userKey);
        }
    }

    public boolean isExists(final String token) {
        String key = tokenKeyPrefix + token;
        return this.exists(key);
    }

    private void deleteExpiredTokensFromSet(String userKey) {
        Set<String> tokens = this.getFromSet(userKey);
        if (tokens != null && !tokens.isEmpty()) {
            // список истекших токенов
            List<String> expiredTokens = tokens.stream()
                    .filter(userToken -> !this.exists(tokenKeyPrefix + userToken))
                    .toList();

            // если есть истёкшие токены, удаляем их все за один раз
            if (!expiredTokens.isEmpty()) {
                redisTemplate.executePipelined((RedisCallback<Void>) connection -> {  // отправка нескольких команд за одно соединение
                    for (String token : expiredTokens) {
                        connection.setCommands().sRem(userKey.getBytes(), token.getBytes());
                    }
                    return null;
                });
            }

            // если множество пустое, то удаляем сам ключ userId
            if (this.getSizeToSet(userKey) == 0) {
                this.delete(userKey);
            }
        }
    }
}
