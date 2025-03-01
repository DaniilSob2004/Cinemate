package com.example.cinemate.service.auth.jwt;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.mapper.AppUserMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccessJwtTokenService {

    @Value("${security.access_token_expiration_time}")
    private Long expirationTime;

    private final JwtTokenService jwtTokenService;
    private final AppUserMapper appUserMapper;

    public AccessJwtTokenService(JwtTokenService jwtTokenService, AppUserMapper appUserMapper) {
        this.jwtTokenService = jwtTokenService;
        this.appUserMapper = appUserMapper;
    }

    public String generateToken(final AppUserJwtDto appUserJwtDto) {
        Map<String, Object> claims = appUserMapper.toClaimsJwt(appUserJwtDto);  // получаем данные польз.
        return jwtTokenService.generateToken(claims, appUserJwtDto.getId().toString(), expirationTime);
    }

    public Integer getIdFromToken(final String token) {
        String strId = jwtTokenService.getSubject(token);
        return Integer.parseInt(strId);
    }

    public AppUserJwtDto extractAllData(final String token) {
        Claims claims = jwtTokenService.getClaims(token);
        return appUserMapper.toAppUserJwtDto(claims);  // получаем данные польз. из claims
    }

    public AppUserJwtDto extractAllDataWithExpiration(final String token) {
        Claims claims = jwtTokenService.getClaimsFromExpired(token);
        return appUserMapper.toAppUserJwtDto(claims);  // получаем данные польз. из claims
    }
}
