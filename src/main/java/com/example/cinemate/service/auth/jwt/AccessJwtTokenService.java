package com.example.cinemate.service.auth.jwt;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.service.redis.token.AccessTokenRedisStorage;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class AccessJwtTokenService {

    @Value("${security.access_token_expiration_time}")
    private Long expirationTime;

    private final AccessTokenRedisStorage accessTokenRedisStorage;
    private final JwtTokenService jwtTokenService;
    private final AppUserMapper appUserMapper;

    public AccessJwtTokenService(AccessTokenRedisStorage accessTokenRedisStorage, JwtTokenService jwtTokenService, AppUserMapper appUserMapper) {
        this.accessTokenRedisStorage = accessTokenRedisStorage;
        this.jwtTokenService = jwtTokenService;
        this.appUserMapper = appUserMapper;
    }

    public String generateToken(final AppUserJwtDto appUserJwtDto) {
        Map<String, Object> claims = appUserMapper.toClaimsJwt(appUserJwtDto);  // получаем данные польз.
        return jwtTokenService.generateToken(claims, appUserJwtDto.getId().toString(), expirationTime);
    }

    public String generateAndSaveToken(final AppUserJwtDto appUserJwtDto) {
        // генерируем и добавляем access token в хранилище
        String accessToken = this.generateToken(appUserJwtDto);
        accessTokenRedisStorage.add(accessToken, appUserJwtDto.getId().toString());
        return accessToken;
    }

    public AppUserJwtDto extractAllData(final String token) {
        Claims claims = jwtTokenService.getClaims(token);
        return appUserMapper.toAppUserJwtDto(claims);  // получаем данные польз. из claims
    }

    public AppUserJwtDto extractAllDataByRequest(final HttpServletRequest request) {
        String token = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing token"));
        return this.extractAllData(token);
    }

    public AppUserJwtDto extractAllDataWithExpiration(final String token) {
        Claims claims = jwtTokenService.getClaimsFromExpired(token);
        return appUserMapper.toAppUserJwtDto(claims);  // получаем данные польз. из claims
    }
}
