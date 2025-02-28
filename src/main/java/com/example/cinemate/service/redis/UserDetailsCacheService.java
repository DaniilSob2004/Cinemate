package com.example.cinemate.service.redis;

import com.example.cinemate.mapper.UserDetailsMapper;
import com.example.cinemate.dto.auth.UserDetailsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserDetailsCacheService extends AbstractRedisRepository<UserDetailsDto> {

    @Value("${redis_data.user_details_prefix}")
    private String userDetailsPrefix;

    @Value("${redis_data.user_details_expiration_time}")
    private long expirationTime;

    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsCacheService(RedisTemplate<String, UserDetailsDto> redisUserDetailsTemplate, UserDetailsMapper userDetailsMapper) {
        super(redisUserDetailsTemplate);
        this.userDetailsMapper = userDetailsMapper;
    }

    public void addToCache(final String id, final UserDetails userDetails) {
        // если объект уже в кэше
        if (this.isHave(id)) {
            return;
        }
        // преобразовываем в DTO
        String key = userDetailsPrefix + id;
        var userDetailsDto = userDetailsMapper.toUserDetailsDto(userDetails);
        this.save(key, userDetailsDto, expirationTime, TimeUnit.SECONDS);
    }

    public Optional<UserDetails> getUserDetails(final String id) {
        // преобразовываем в UserDetails
        String key = userDetailsPrefix + id;
        var userDetailsDto = this.get(key);
        return Optional.ofNullable(userDetailsDto).map(userDetailsMapper::toUserDetails);
    }

    public void remove(final String id) {
        String key = userDetailsPrefix + id;
        this.delete(key);
    }

    public boolean isHave(final String id) {
        String key = userDetailsPrefix + id;
        return this.exists(key);
    }
}
