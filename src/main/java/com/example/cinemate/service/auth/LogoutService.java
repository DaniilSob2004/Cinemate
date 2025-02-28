package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.LogoutRequestDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.redis.BlacklistTokenRedisService;
import com.example.cinemate.service.redis.RefreshTokenRedisStorage;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogoutService {

    private final JwtTokenService jwtTokenService;
    private final BlacklistTokenRedisService blacklistTokenRedisService;
    private final RefreshTokenRedisStorage refreshTokenRedisStorage;

    public LogoutService(JwtTokenService jwtTokenService, BlacklistTokenRedisService blacklistTokenRedisService, RefreshTokenRedisStorage refreshTokenRedisStorage) {
        this.jwtTokenService = jwtTokenService;
        this.blacklistTokenRedisService = blacklistTokenRedisService;
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
    }

    public void logoutUser(final LogoutRequestDto logoutRequestDto, final HttpServletRequest request) {
        // проверка токенов на валидность
        String accessToken = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing access token"));

        if (!jwtTokenService.validateTokenWithoutExpiration(logoutRequestDto.getRefreshToken())) {
            throw new UnauthorizedException("Invalid or missing refresh token");
        }

        Logger.info("-------- Access token in logout service (" + accessToken + ") --------");
        Logger.info("-------- Refresh token in logout service (" + logoutRequestDto.getRefreshToken() + ") --------");

        // добавление access token в 'Redis' blacklist (чтобы до истечение срока нельзя было его использ.)
        blacklistTokenRedisService.addToBlacklist(accessToken);

        // удаление refresh token из хранилища Redis
        refreshTokenRedisStorage.remove(logoutRequestDto.getRefreshToken());
    }
}
