package com.example.cinemate.service.auth.jwt;

import com.example.cinemate.dto.auth.RefreshTokenDto;
import com.example.cinemate.mapper.user.UserMapper;
import com.example.cinemate.service.redis.token.RefreshTokenRedisStorage;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefreshJwtTokenService {

    @Value("${security.refresh_token_expiration_time}")
    private Long expirationTime;

    private final RefreshTokenRedisStorage refreshTokenRedisStorage;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

    public RefreshJwtTokenService(RefreshTokenRedisStorage refreshTokenRedisStorage, JwtTokenService jwtTokenService, UserMapper userMapper) {
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
        this.jwtTokenService = jwtTokenService;
        this.userMapper = userMapper;
    }

    public String generateToken(final RefreshTokenDto refreshTokenDto) {
        Map<String, Object> claims = userMapper.toClaimsJwt(refreshTokenDto);  // получаем данные польз.
        return jwtTokenService.generateToken(claims, refreshTokenDto.getId().toString(), expirationTime);
    }

    public String generateAndSaveToken(final RefreshTokenDto refreshTokenDto) {
        // генерируем и добавляем refresh token в хранилище
        String refreshToken = this.generateToken(refreshTokenDto);
        refreshTokenRedisStorage.add(refreshToken, refreshTokenDto.getId().toString());
        return refreshToken;
    }

    public RefreshTokenDto extractAllData(final String token) {
        Claims claims = jwtTokenService.getClaims(token);
        return userMapper.toRefreshTokenDto(claims);  // получаем данные польз. из claims
    }
}
