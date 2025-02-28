package com.example.cinemate.service.auth;

import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.Role;
import com.example.cinemate.model.db.UserRole;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.roleservice.RoleService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.validate.RegisterValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegisterService {

    @Value("${user_data.role}")
    private String nameUserRole;

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final AuthService authService;
    private final AppUserMapper appUserMapper;
    private final RegisterValidate registerValidate;

    public RegisterService(BCryptPasswordEncoder passwordEncoder, AppUserService appUserService, RoleService roleService, UserRoleService userRoleService, AuthService authService, AppUserMapper appUserMapper, RegisterValidate registerValidate) {
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
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

        return this.authenticateAndGenerateToken(user.getId());  // авторизация и генерация токена
    }

    public String authenticateAndGenerateToken(final Integer id) {
        return authService.authenticateAndGenerateToken(id.toString(), null, true);
    }

    public void createUser(final AppUser user) {
        appUserService.save(user);
        this.addUserRole(user);
    }

    private void addUserRole(final AppUser user) {
        Role userRole = roleService.findRoleByName(nameUserRole)  // используется кеш
                .orElseThrow(() -> new RuntimeException(nameUserRole + " not found..."));
        UserRole roleForUser = new UserRole(null, user, userRole);
        userRoleService.save(roleForUser);
    }
}
