package com.example.cinemate.config;

import com.example.cinemate.dto.auth.UserDetailsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String hostName;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${redis_data.base_redis_expiration_time}")
    private long expirationTime;

    // фабрика соединения для кэширования (БД 0)
    @Bean
    public RedisConnectionFactory redisCacheConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(0);
        configuration.setHostName(hostName);
        configuration.setPort(port);
        return new LettuceConnectionFactory(configuration);
    }

    // фабрика соединения для refresh_token (БД 1)
    @Bean
    public RedisConnectionFactory redisRefreshTokenConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(1);
        configuration.setHostName(hostName);
        configuration.setPort(port);
        return new LettuceConnectionFactory(configuration);
    }

    // (для кэширования результатов методов) (БД 0) Spring будет использ. для управления кэшированием данных с использ. Redis
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisCacheConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(expirationTime));

        return RedisCacheManager.builder(redisCacheConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    // для сохранения токенов в blacklist (БД 0)
    @Bean
    public RedisTemplate<String, String> redisBlacklistTokenTemplate(RedisConnectionFactory redisCacheConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisCacheConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    // для кэширования UserDetails (БД 0)
    @Bean
    public RedisTemplate<String, UserDetailsDto> redisUserDetailsTemplate(RedisConnectionFactory redisCacheConnectionFactory) {
        RedisTemplate<String, UserDetailsDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisCacheConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserDetailsDto.class));
        return template;
    }

    // для работы с refresh_token (БД 1)
    @Bean
    public RedisTemplate<String, String> redisRefreshTokenTemplate(RedisConnectionFactory redisRefreshTokenConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisRefreshTokenConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    // по дефолту (БД 0)
    @Bean
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory redisCacheConnectionFactory) {
        RedisTemplate<String, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisCacheConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserDetailsDto.class));
        return template;
    }
}
