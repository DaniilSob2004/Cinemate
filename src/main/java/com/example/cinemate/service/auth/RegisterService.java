package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.model.AppUser;
import com.example.cinemate.model.Role;
import com.example.cinemate.model.UserRole;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.roleservice.RoleService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegisterService {

    @Value("${user_data.role}")
    private String nameUserRole;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Transactional
    public String registerUser(final RegisterRequestDto registerRequestDto) throws Exception {
        // проверка данных
        String errorMessage = this.checkRegisterData(registerRequestDto).orElse(null);
        if (errorMessage != null) {
            throw new Exception(errorMessage);
        }

        // добавление пользователя в БД
        AppUser user = this.createNewUser(registerRequestDto);
        appUserService.save(user);

        // добавление роли пользователя в БД
        Role userRole = roleService.findRoleByName(nameUserRole).orElseThrow(() -> new RuntimeException(nameUserRole + " not found..."));
        UserRole roleForUser = new UserRole(null, user, userRole);
        userRoleService.save(roleForUser);

        // авторизация и генерация токена
        return authService.authenticateAndGenerateToken(user.getEmail(), registerRequestDto.getPassword());
    }

    private Optional<String> checkRegisterData(final RegisterRequestDto registerRequestDto) {
        // есть ли такой пользователь в бд
        AppUser user = appUserService.findByEmail(registerRequestDto.getEmail()).orElse(null);
        if (user != null) {
            return Optional.of("A user with such an email already exists");
        }

        // проверяем пароли на совпадение
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            return Optional.of("Passwords do not match");
        }

        return Optional.empty();
    }

    private AppUser createNewUser(final RegisterRequestDto registerRequestDto) {
        return new AppUser(
                null,
                "",
                "",
                "",
                registerRequestDto.getEmail(),
                "",
                bCryptPasswordEncoder.encode(registerRequestDto.getPassword()),
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }
}
