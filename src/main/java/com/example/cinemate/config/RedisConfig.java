package com.example.cinemate.config;

import com.example.cinemate.dto.auth.UserDetailsDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisBlacklistTokenTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // устанавливает сериализатор для ключа и значения
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, UserDetailsDto> redisUserDetailsTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserDetailsDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // устанавливает сериализатор для ключа и значения
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(UserDetailsDto.class));

        return template;
    }

    @Bean
    public RedisTemplate<String, List<String>> redisUserRolesTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<String>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // устанавливает сериализатор для ключа и значения
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));

        return template;
    }
}
