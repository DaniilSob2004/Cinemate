package com.example.cinemate.service.redis;

import com.example.cinemate.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class BlacklistTokenRedisService extends AbstractRedisRepository<String> {

    @Value("${redis_data.blacklist_token_value}")
    private String blacklistTokenValue;

    @Value("${redis_data.blacklist_token_key_prefix}")
    private String blacklistTokenKeyPrefix;

    private final JwtTokenUtil jwtTokenUtil;

    public BlacklistTokenRedisService(RedisTemplate<String, String> redisBlacklistTokenTemplate, JwtTokenUtil jwtTokenUtil) {
        super(redisBlacklistTokenTemplate);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void addToBlacklist(final String token) {
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        long ttl = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

        if (ttl > 0 && !this.exists(token)) {
            String key = blacklistTokenKeyPrefix + token;
            this.save(key, blacklistTokenValue, ttl, TimeUnit.SECONDS);
        }
    }

    public boolean isBlacklisted(final String token) {
        String key = blacklistTokenKeyPrefix + token;
        return this.exists(key);
    }
}
