package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.model.AuthenticationRequest;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business.userservice.SaveUserService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RegisterService {

    @Value("${user_data.role}")
    private String nameUserRole;

    private final AuthService authService;
    private final SaveUserService saveUserService;
    private final UserDataValidate userDataValidate;
    private final AppUserMapper appUserMapper;

    public RegisterService(AuthService authService, SaveUserService saveUserService, UserDataValidate userDataValidate, AppUserMapper appUserMapper) {
        this.authService = authService;
        this.saveUserService = saveUserService;
        this.userDataValidate = userDataValidate;
        this.appUserMapper = appUserMapper;
    }

    @Transactional
    public ResponseAuthDto registerUser(final RegisterRequestDto registerRequestDto) {
        // валидация
        userDataValidate.validateUserExistence(registerRequestDto.getEmail());

        // создаём пользователя
        AppUser user = appUserMapper.toAppUser(registerRequestDto);
        this.createUser(user);

        return this.authenticateAndGenerateToken(user.getId(), "");  // авторизация и генерация токена
    }

    public ResponseAuthDto authenticateAndGenerateToken(final Integer id, final String provider) {
        var authRequest = new AuthenticationRequest(
                id.toString(),
                null,
                true,
                provider);
        return authService.authenticateAndGenerateToken(authRequest);
    }

    public void createUser(final AppUser user) {
        saveUserService.createUser(user);
        saveUserService.createUserRoles(user, List.of(nameUserRole));
    }
}
