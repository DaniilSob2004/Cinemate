package com.example.cinemate.service.auth.userdetail;

import com.example.cinemate.mapper.GrantedAuthorityMapper;
import com.example.cinemate.mapper.UserDetailsMapper;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.service.redis.UserDetailsCacheService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserService appUserService;
    private final UserRoleService userRoleService;
    private final UserDetailsCacheService userDetailsCacheService;
    private final UserDataValidate userDataValidate;
    private final UserDetailsMapper userDetailsMapper;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public UserDetailsServiceImpl(AppUserService appUserService, UserRoleService userRoleService, UserDetailsCacheService userDetailsCacheService, UserDataValidate userDataValidate, UserDetailsMapper userDetailsMapper, GrantedAuthorityMapper grantedAuthorityMapper) {
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.userDataValidate = userDataValidate;
        this.userDetailsMapper = userDetailsMapper;
        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Logger.info("!!! CREATE UserDetails (by username) !!!");
        AppUser user = appUserService.findByEmailWithoutIsActive(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + "' was not found..."));

        // проверка активирован ли пользователь (иначе UserInactiveException)
        userDataValidate.validateIsActiveUser(user);

        return this.createUserDetails(user, null);
    }

    @Transactional
    public UserDetails loadUserById(final Integer id, final List<String> roles) {
        // находим пользователя в кэше
        return userDetailsCacheService.getUserDetails(id.toString())
                .map(user -> {
                    Logger.info("UserDetails from Redis cache");
                    return user;
                })
                .orElseGet(() -> {
                    Logger.info("!!! CREATE UserDetails (by ID) !!!");
                    AppUser user = appUserService.findByIdWithoutIsActive(id)
                            .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

                    // проверка активирован ли пользователь (иначе UserInactiveException)
                    userDataValidate.validateIsActiveUser(user);

                    return createUserDetails(user, roles);
                });
    }

    public void addUserToCache(final Integer id, final UserDetails userDetails) {
        userDetailsCacheService.addToCache(id.toString(), userDetails);
    }

    private UserDetails createUserDetails(final AppUser user, final List<String> roles) {
        // установка ролей для данного пользователя
        List<GrantedAuthority> grantList = (roles == null)
                ? this.getRolesForUser(user.getId())
                : grantedAuthorityMapper.toGrantedAuthorities(roles);

        // возвращаем объект внутреннего Spring UserDetails
        return userDetailsMapper.toCustomUserDetails(user, grantList);
    }

    private List<GrantedAuthority> getRolesForUser(final Integer id) {
        List<String> roleNames = userRoleService.getRoleNames(id);  // используется кеш
        return grantedAuthorityMapper.toGrantedAuthorities(roleNames);
    }
}
