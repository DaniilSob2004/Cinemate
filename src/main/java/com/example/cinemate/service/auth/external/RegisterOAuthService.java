package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.dto.auth.OAuthUserDto;
import com.example.cinemate.dto.user.file.UserFilesBufferDto;
import com.example.cinemate.exception.auth.OAuthException;
import com.example.cinemate.exception.auth.UserInactiveException;
import com.example.cinemate.mapper.user.UserMapper;
import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.auth.RegisterService;
import com.example.cinemate.service.business.common.UploadFilesAsyncService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import com.example.cinemate.utils.FileWorkUtil;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;

@Service
public class RegisterOAuthService {

    private final AppUserService appUserService;
    private final ExternalAuthService externalAuthService;
    private final RegisterService registerService;
    private final UploadFilesAsyncService uploadFilesAsyncService;
    private final UserMapper userMapper;
    private final OAuthUserMapper oAuthUserMapper;

    public RegisterOAuthService(AppUserService appUserService, ExternalAuthService externalAuthService, RegisterService registerService, UploadFilesAsyncService uploadFilesAsyncService, UserMapper userMapper, OAuthUserMapper oAuthUserMapper) {
        this.appUserService = appUserService;
        this.externalAuthService = externalAuthService;
        this.registerService = registerService;
        this.uploadFilesAsyncService = uploadFilesAsyncService;
        this.userMapper = userMapper;
        this.oAuthUserMapper = oAuthUserMapper;
    }

    @Transactional
    public ResponseAuthDto registerUser(final OAuthUserDto oAuthUserDto) {
        oAuthUserDto.setEmail(oAuthUserDto.getEmail().toLowerCase());

        // если пользователь есть
        AppUser user = appUserService.findByEmailWithoutIsActive(oAuthUserDto.getEmail()).orElse(null);
        if (user != null) {
            // если пользователь неактивный
            if (!user.getIsActive()) {
                throw new UserInactiveException("User is inactive...");
            }

            var externalAuth = externalAuthService.findByProviderNameAndExternalId(
                    oAuthUserDto.getProvider().getName(),
                    oAuthUserDto.getExternalId()
            ).orElse(null);

            // есть ли уже авторизация через другой внешний провайдер
            if (externalAuth == null) {
                throw new OAuthException("This email is already associated with another OAuth provider");
            }

            // обновляем запись в ExternalAuth
            externalAuth.setAccessToken(oAuthUserDto.getAccessToken());
            externalAuthService.update(externalAuth);
        }
        else {  // если пользователя нет, то создаём
            user = this.createNewUserFromOAuth(oAuthUserDto);
        }

        return registerService.authenticateAndGenerateToken(user.getId(), oAuthUserDto.getProvider().getName());  // авторизация и генерация токена
    }

    private AppUser createNewUserFromOAuth(final OAuthUserDto oAuthUserDto) {
        Logger.info("-------- Register oAuth (" + oAuthUserDto + ") --------");

        AppUser user = userMapper.toAppUser(oAuthUserDto);

        var savedUser = registerService.createUser(user);
        this.createExternalAuth(oAuthUserDto, user);

        // получение и сохранение аватарки от Google
        this.uploadFilesAndUpdate(savedUser, oAuthUserDto.getPicture());

        return savedUser;
    }

    private void uploadFilesAndUpdate(final AppUser user, final String avatarUrl) {
        // загружаем аватарку в s3
        var tempContentFile = FileWorkUtil.downloadImageAsTempFile(avatarUrl);
        tempContentFile.ifPresent(contentFile ->
                uploadFilesAsyncService.uploadUserFilesAndUpdate(user, new UserFilesBufferDto(tempContentFile.get()))
        );
    }

    private void createExternalAuth(final OAuthUserDto oAuthUserDto, final AppUser user) {
        var newExternalAuth = oAuthUserMapper.toExternalAuth(oAuthUserDto, user);
        externalAuthService.save(newExternalAuth);
    }
}
