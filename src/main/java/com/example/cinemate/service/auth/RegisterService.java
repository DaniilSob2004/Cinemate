package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.OAuthUserDto;
import com.example.cinemate.exception.auth.OAuthException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.ExternalAuth;
import com.example.cinemate.model.db.Role;
import com.example.cinemate.model.db.UserRole;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import com.example.cinemate.service.business_db.roleservice.RoleService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.validate.RegisterValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegisterService {

    @Value("${user_data.role}")
    private String nameUserRole;

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserService appUserService;
    private final ExternalAuthService externalAuthService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final AuthService authService;
    private final AppUserMapper appUserMapper;
    private final RegisterValidate registerValidate;

    public RegisterService(BCryptPasswordEncoder passwordEncoder, AppUserService appUserService, ExternalAuthService externalAuthService, RoleService roleService, UserRoleService userRoleService, AuthService authService, AppUserMapper appUserMapper, RegisterValidate registerValidate) {
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
        this.externalAuthService = externalAuthService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.authService = authService;
        this.appUserMapper = appUserMapper;
        this.registerValidate = registerValidate;
    }

    @Transactional
    public String registerUser(final RegisterRequestDto registerRequestDto) {
        registerRequestDto.setEmail(registerRequestDto.getEmail().toLowerCase());

        // проверка данных (если ошибка, то будет исключение)
        registerValidate.validateRegisterData(registerRequestDto);

        // создаём пользователя
        String password = passwordEncoder.encode(registerRequestDto.getPassword());
        AppUser user = appUserMapper.toAppUser(registerRequestDto, password);
        this.createUser(user);

        return this.authenticateAndGenerateToken(user);  // авторизация и генерация токена
    }

    @Transactional
    public String registerUserWithOAuth(final OAuthUserDto oAuthUserDto) {
        oAuthUserDto.setEmail(oAuthUserDto.getEmail().toLowerCase());

        // если пользователь есть
        AppUser user = appUserService.findByEmail(oAuthUserDto.getEmail()).orElse(null);
        if (user != null) {
            // если авторизация через другой внешний провайдер
            if (!externalAuthService.existsByProviderAndExternalId(
                    oAuthUserDto.getProvider().getName(),
                    oAuthUserDto.getExternalId()
            )) {
                throw new OAuthException("This email is already associated with another OAuth provider");
            }
        }
        else {  // если пользователь нет, то создаём
            user = this.createNewUserFromOAuth(oAuthUserDto);
        }

        return this.authenticateAndGenerateToken(user);  // авторизация и генерация токена
    }

    private String authenticateAndGenerateToken(final AppUser user) {
        return authService.authenticateAndGenerateToken(user.getId().toString(), null, true);
    }

    private AppUser createNewUserFromOAuth(final OAuthUserDto oAuthUserDto) {
        String password = passwordEncoder.encode(GenerateUtil.getRandomString());
        AppUser user = appUserMapper.toAppUser(oAuthUserDto, password);
        this.createUser(user);
        this.createExternalAuth(oAuthUserDto, user);
        return user;
    }

    private void createUser(final AppUser user) {
        appUserService.save(user);
        this.addUserRole(user);
    }

    private void createExternalAuth(final OAuthUserDto oAuthUserDto, final AppUser user) {
        var newExternalAuth = new ExternalAuth(null, user, oAuthUserDto.getProvider(), oAuthUserDto.getExternalId(), LocalDateTime.now());
        externalAuthService.save(newExternalAuth);
    }

    private void addUserRole(final AppUser user) {
        Role userRole = roleService.findRoleByName(nameUserRole)
                .orElseThrow(() -> new RuntimeException(nameUserRole + " not found..."));
        UserRole roleForUser = new UserRole(null, user, userRole);
        userRoleService.save(roleForUser);
    }
}
