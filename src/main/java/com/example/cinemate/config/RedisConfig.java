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


    // создание фабрики соединения по указанному index db
    private RedisConnectionFactory createRedisConnectionFactory(int indexDb) {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(indexDb);
        configuration.setHostName(hostName);
        configuration.setPort(port);
        return new LettuceConnectionFactory(configuration);
    }

    // фабрика соединения для кэширования (БД 0)
    @Bean
    public RedisConnectionFactory redisCacheConnectionFactory() {
        return createRedisConnectionFactory(0);
    }

    // фабрика соединения для access_token (БД 1)
    @Bean
    public RedisConnectionFactory redisAccessTokenConnectionFactory() {
        return createRedisConnectionFactory(1);
    }

    // фабрика соединения для refresh_token (БД 2)
    @Bean
    public RedisConnectionFactory redisRefreshTokenConnectionFactory() {
        return createRedisConnectionFactory(2);
    }

    // фабрика соединения для reset_password_token (БД 3)
    @Bean
    public RedisConnectionFactory redisResetPasswordTokenConnectionFactory() {
        return createRedisConnectionFactory(3);
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

    // для кэширования UserDetails (БД 0)
    @Bean
    public RedisTemplate<String, UserDetailsDto> redisUserDetailsTemplate(RedisConnectionFactory redisCacheConnectionFactory) {
        RedisTemplate<String, UserDetailsDto> template = new RedisTemplate<>();
        template.setConnectionFactory(redisCacheConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserDetailsDto.class));
        return template;
    }

    // для работы с user_provider (БД 0)
    @Bean
    public RedisTemplate<String, String> redisUserProviderTemplate(RedisConnectionFactory redisCacheConnectionFactory) {
        return this.getStringsRedisTemplate(redisCacheConnectionFactory);
    }

    // для работы с access_token (БД 1)
    @Bean
    public RedisTemplate<String, String> redisAccessTokenTemplate(RedisConnectionFactory redisAccessTokenConnectionFactory) {
        return this.getStringsRedisTemplate(redisAccessTokenConnectionFactory);
    }

    // для работы с refresh_token (БД 2)
    @Bean
    public RedisTemplate<String, String> redisRefreshTokenTemplate(RedisConnectionFactory redisRefreshTokenConnectionFactory) {
        return this.getStringsRedisTemplate(redisRefreshTokenConnectionFactory);
    }

    // для работы с reset_password_token (БД 3)
    @Bean
    public RedisTemplate<String, String> redisResetPasswordTokenTemplate(RedisConnectionFactory redisResetPasswordTokenConnectionFactory) {
        return this.getStringsRedisTemplate(redisResetPasswordTokenConnectionFactory);
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


    private RedisTemplate<String, String> getStringsRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
