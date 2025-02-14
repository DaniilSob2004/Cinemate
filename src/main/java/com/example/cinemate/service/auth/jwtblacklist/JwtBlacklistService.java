package com.example.cinemate.service.auth.jwtblacklist;

import com.example.cinemate.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

// Использование Redis для кэширование токенов logout пользователей
@Service
public class JwtBlacklistService {

    @Value("${redis_data.blacklist_token_value}")
    private String blacklistTokenValue;

    @Value("${redis_data.blacklist_token_key_prefix}")
    private String blacklistTokenKeyPrefix;

    @Autowired
    private RedisTemplate<String, String> redisBlacklistTokenTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void addToBlacklist(final String token) {
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        long ttl = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

        if (ttl > 0 && !this.isBlacklisted(token)) {
            redisBlacklistTokenTemplate.opsForValue().set(blacklistTokenKeyPrefix + token, blacklistTokenValue, ttl, TimeUnit.SECONDS);
        }
    }

    public boolean isBlacklisted(final String token) {
        return Boolean.TRUE.equals(redisBlacklistTokenTemplate.hasKey(blacklistTokenKeyPrefix + token));
    }
}
