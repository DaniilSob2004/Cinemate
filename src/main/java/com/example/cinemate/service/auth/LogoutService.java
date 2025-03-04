package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.LogoutRequestDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.redis.token.AccessTokenRedisStorage;
import com.example.cinemate.service.redis.token.RefreshTokenRedisStorage;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogoutService {

    private final JwtTokenService jwtTokenService;
    private final AccessTokenRedisStorage accessTokenRedisStorage;
    private final RefreshTokenRedisStorage refreshTokenRedisStorage;

    public LogoutService(JwtTokenService jwtTokenService, AccessTokenRedisStorage accessTokenRedisStorage, RefreshTokenRedisStorage refreshTokenRedisStorage) {
        this.jwtTokenService = jwtTokenService;
        this.accessTokenRedisStorage = accessTokenRedisStorage;
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
    }

    public void logoutUser(final LogoutRequestDto logoutRequestDto, final HttpServletRequest request) {
        // проверка токенов на валидность
        String accessToken = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing access token"));

        if (!jwtTokenService.validateTokenWithoutExpiration(logoutRequestDto.getRefreshToken())) {
            throw new UnauthorizedException("Invalid or missing refresh token");
        }

        // если такого токена нет в хранилище
        if (!refreshTokenRedisStorage.isExists(logoutRequestDto.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token is incorrect");
        }

        Logger.info("-------- Access token in logout service (" + accessToken + ") --------");
        Logger.info("-------- Refresh token in logout service (" + logoutRequestDto.getRefreshToken() + ") --------");

        // удаление access token из 'Redis' (делаем его недоступным больше)
        accessTokenRedisStorage.removeByToken(accessToken);

        // удаление refresh token из хранилища Redis
        refreshTokenRedisStorage.removeByToken(logoutRequestDto.getRefreshToken());
    }
}
