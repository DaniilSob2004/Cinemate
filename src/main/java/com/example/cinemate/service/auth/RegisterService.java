package com.example.cinemate.service.auth;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.exception.auth.PasswordMismatchException;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.model.AppUser;
import com.example.cinemate.model.Role;
import com.example.cinemate.model.UserRole;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.roleservice.RoleService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegisterService {

    @Value("${user_data.role}")
    private String nameUserRole;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AppUserConvertDto appUserConvertDto;

    @Transactional
    public String registerUser(final RegisterRequestDto registerRequestDto) {
        registerRequestDto.setEmail(registerRequestDto.getEmail().toLowerCase());

        // проверка данных (если ошибка, то будет исключение)
        this.checkRegisterData(registerRequestDto);

        // добавление пользователя в БД
        AppUser user = appUserConvertDto.convertToAppUser(registerRequestDto);
        appUserService.save(user);

        // добавление роли пользователя в БД
        this.addUserRole(user);

        // авторизация и генерация токена
        return authService.authenticateAndGenerateToken(user.getEmail(), registerRequestDto.getPassword());
    }

    @Transactional
    public String registerUser(final GoogleUserAuthDto googleUserAuthDto) {
        googleUserAuthDto.setEmail(googleUserAuthDto.getEmail().toLowerCase());

        // проверка данных (если ошибка, то будет исключение)
        this.checkUserInAuth(googleUserAuthDto.getEmail());

        // добавление пользователя в БД
        AppUser user = appUserConvertDto.convertToAppUser(googleUserAuthDto);
        appUserService.save(user);

        // добавление роли пользователя в БД
        this.addUserRole(user);

        // авторизация и генерация токена
        return authService.authenticateAndGenerateToken(user.getEmail(), "");
    }

    private void addUserRole(final AppUser user) {
        Role userRole = roleService.findRoleByName(nameUserRole)
                .orElseThrow(() -> new RuntimeException(nameUserRole + " not found..."));
        UserRole roleForUser = new UserRole(null, user, userRole);
        userRoleService.save(roleForUser);
    }

    private void checkRegisterData(final RegisterRequestDto registerRequestDto) {
        // есть ли такой пользователь в бд
        this.checkUserInAuth(registerRequestDto.getEmail());

        // проверяем пароли на совпадение
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }

    private void checkUserInAuth(final String email) {
        // есть ли такой пользователь в бд
        appUserService.findByEmail(email)
                .ifPresent((user) -> { throw new UserAlreadyExistsException("User already exists"); });
    }
}
