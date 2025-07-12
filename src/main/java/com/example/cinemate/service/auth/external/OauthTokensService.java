package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.OauthStateDto;
import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.exception.common.NotFoundException;
import com.example.cinemate.service.redis.AuthProviderTokenStorage;
import org.springframework.stereotype.Service;

@Service
public class OauthTokensService {

    private final AuthProviderTokenStorage authProviderTokenStorage;

    public OauthTokensService(AuthProviderTokenStorage authProviderTokenStorage) {
        this.authProviderTokenStorage = authProviderTokenStorage;
    }

    public ResponseAuthDto getTokensByState(final OauthStateDto oauthStateDto) {
        var responseAuthDto = authProviderTokenStorage.getResponseAuthDto(oauthStateDto.getState())
                .orElseThrow(() -> new NotFoundException("Token not found"));

        authProviderTokenStorage.remove(oauthStateDto.getState());

        return responseAuthDto;
    }
}
