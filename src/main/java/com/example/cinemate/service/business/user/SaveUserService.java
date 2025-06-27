package com.example.cinemate.service.business.user;

import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveUserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserService appUserService;
    private final UserRoleService userRoleService;
    private final UserDataValidate userDataValidate;

    public SaveUserService(BCryptPasswordEncoder passwordEncoder, AppUserService appUserService, UserRoleService userRoleService, UserDataValidate userDataValidate) {
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.userDataValidate = userDataValidate;
    }

    public AppUser createUser(final AppUser user) {
        this.prepareUserDataForSave(user);
        return appUserService.save(user);
    }

    public void createUserRoles(final AppUser user, List<String> rolesName) {
        for (String roleName : rolesName) {
            userRoleService.saveByRoleName(user, roleName);
        }
    }

    private void prepareUserDataForSave(final AppUser user) {
        user.setUsername(
                userDataValidate.normalizeUsername(user.getUsername(), user.getEmail())
        );
        user.setEmail(
                user.getEmail().toLowerCase()
        );
        if (user.getEncPassword() != null) {
            user.setEncPassword(
                    passwordEncoder.encode(user.getEncPassword())
            );
        }
    }
}
