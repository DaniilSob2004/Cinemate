package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.dto.auth.RefreshTokenDto;
import com.example.cinemate.dto.auth.UpdateAccessTokenDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.auth.jwt.RefreshJwtTokenService;
import com.example.cinemate.service.redis.RefreshTokenRedisStorage;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class UpdateTokenService {

    private final JwtTokenService jwtTokenService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final RefreshJwtTokenService refreshJwtTokenService;
    private final RefreshTokenRedisStorage refreshTokenRedisStorage;

    public UpdateTokenService(JwtTokenService jwtTokenService, AccessJwtTokenService accessJwtTokenService, RefreshJwtTokenService refreshJwtTokenService, RefreshTokenRedisStorage refreshTokenRedisStorage) {
        this.jwtTokenService = jwtTokenService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.refreshJwtTokenService = refreshJwtTokenService;
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
    }

    public String updateAccessToken(final UpdateAccessTokenDto updateAccessTokenDto, final HttpServletRequest request) {
        // извлекаем и проверяем refresh_token
        String refreshToken = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing refresh token"));

        // проверка access_token (без учёта истечения)
        if (!jwtTokenService.validateTokenWithoutExpiration(updateAccessTokenDto.getAccessToken())) {
            throw new BadRequestException("Invalid or missing access token");
        }

        // декодируем данные из токенов
        AppUserJwtDto oldAppUserJwtDto = accessJwtTokenService.extractAllDataWithExpiration(updateAccessTokenDto.getAccessToken());
        RefreshTokenDto refreshTokenDto = refreshJwtTokenService.extractAllData(refreshToken);

        // проверка принадлежат ли токены одному пользователю
        if (!Objects.equals(oldAppUserJwtDto.getId(), refreshTokenDto.getId())) {
            throw new UnauthorizedException("The access token and refresh token belong to different users");
        }

        // проверка есть ли refresh_token в Redis
        if (!refreshTokenRedisStorage.isHave(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        // генерация нового access_token
        return accessJwtTokenService.generateToken(oldAppUserJwtDto);
    }
}
