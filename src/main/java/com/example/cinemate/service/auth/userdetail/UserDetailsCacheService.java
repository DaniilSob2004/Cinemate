package com.example.cinemate.service.auth.userdetail;

import com.example.cinemate.convert.UserDetailsConvertDto;
import com.example.cinemate.dto.auth.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

// Использование Redis для кэширование объектов UserDetails
@Service
public class UserDetailsCacheService {

    @Value("${redis_data.user_details_prefix}")
    private String userDetailsPrefix;

    @Value("${redis_data.user_details_expiration_time}")
    private long expirationTime;

    @Autowired
    private RedisTemplate<String, UserDetailsDto> redisUserDetailsTemplate;

    @Autowired
    private UserDetailsConvertDto userDetailsConvertDto;

    public void addToCache(final String username, final UserDetails userDetails) {
        if (this.isHave(username)) {
            return;
        }
        // преобразовываем в DTO
        var userDetailsDto = userDetailsConvertDto.convertToUserDetailsDto(userDetails);
        String key = userDetailsPrefix + username;
        redisUserDetailsTemplate.opsForValue().set(key, userDetailsDto, expirationTime, TimeUnit.SECONDS);
    }

    public Optional<UserDetails> get(final String username) {
        // преобразовываем в UserDetails
        String key = userDetailsPrefix + username;
        var userDetailsDto = redisUserDetailsTemplate.opsForValue().get(key);
        return Optional.ofNullable(userDetailsDto)
                .map(userDetailsConvertDto::convertToUserDetails);
    }

    public void remove(final String username) {
        String key = userDetailsPrefix + username;
        redisUserDetailsTemplate.delete(key);
    }

    public boolean isHave(final String username) {
        String key = userDetailsPrefix + username;
        return Boolean.TRUE.equals(redisUserDetailsTemplate.hasKey(key));
    }
}
