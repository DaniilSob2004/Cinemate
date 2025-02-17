package com.example.cinemate.service.auth;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.exception.auth.PasswordMismatchException;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.Role;
import com.example.cinemate.model.db.UserRole;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.roleservice.RoleService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import com.example.cinemate.utils.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public String registerUser(final RegisterRequestDto registerRequestDto) {
        // в нижний регистр
        registerRequestDto.setEmail(registerRequestDto.getEmail().toLowerCase());

        // проверка данных (если ошибка, то будет исключение)
        this.checkRegisterData(registerRequestDto);

        // регистрация и генерация токена
        String password = bCryptPasswordEncoder.encode(registerRequestDto.getPassword());
        AppUser user = appUserConvertDto.convertToAppUser(registerRequestDto, password);
        return this.registerAndGenerateToken(user);
    }

    @Transactional
    public String registerUser(final GoogleUserAuthDto googleUserAuthDto) {
        // в нижний регистр
        googleUserAuthDto.setEmail(googleUserAuthDto.getEmail().toLowerCase());

        // регистрация и генерация токена
        String password = bCryptPasswordEncoder.encode(GenerateUtil.getRandomString());
        AppUser user = appUserConvertDto.convertToAppUser(googleUserAuthDto, password);
        return this.registerAndGenerateToken(user);
    }

    private String registerAndGenerateToken(final AppUser user) {
        appUserService.save(user);  // добавление пользователя
        this.addUserRole(user);  // добавление роли пользователя в БД
        return authService.authenticateAndGenerateToken(user.getId().toString(), null, true);  // авторизация и генерация токена
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
