package com.example.cinemate.service.redis;

import com.example.cinemate.convert.GrantedAuthorityConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserRoleCacheService extends AbstractRedisRepository<List<String>> {

    @Value("${redis_data.user_roles_prefix}")
    private String userRolesPrefix;

    @Value("${redis_data.user_roles_expiration_time}")
    private long expirationTime;

    @Autowired
    private GrantedAuthorityConvert grantedAuthorityConvert;

    public UserRoleCacheService(RedisTemplate<String, List<String>> redisUserRolesTemplate) {
        super(redisUserRolesTemplate);
    }

    public void addToCache(final String id, final List<GrantedAuthority> authorities) {
        // если объект уже в кэше
        if (this.isHave(id)) {
            return;
        }
        String key = userRolesPrefix + id;
        List<String> strAuthorities = grantedAuthorityConvert.convertToStringList(authorities);
        this.save(key, strAuthorities, expirationTime, TimeUnit.SECONDS);
    }

    public Optional<List<String>> getRoles(final String id) {
        String key = userRolesPrefix + id;
        List<String> authorities = this.get(key);
        return Optional.ofNullable(authorities);
    }

    public boolean isHave(final String id) {
        String key = userRolesPrefix + id;
        return this.exists(key);
    }
}
