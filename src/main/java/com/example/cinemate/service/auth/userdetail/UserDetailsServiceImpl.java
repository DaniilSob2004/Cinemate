package com.example.cinemate.service.auth.userdetail;

import com.example.cinemate.mapper.GrantedAuthorityMapper;
import com.example.cinemate.mapper.UserDetailsMapper;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import com.example.cinemate.service.redis.UserDetailsCacheService;
import com.example.cinemate.service.redis.UserRoleCacheService;
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
    private final UserRoleCacheService userRoleCacheService;
    private final UserDetailsMapper userDetailsMapper;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public UserDetailsServiceImpl(AppUserService appUserService, UserRoleService userRoleService, UserDetailsCacheService userDetailsCacheService, UserRoleCacheService userRoleCacheService, UserDetailsMapper userDetailsMapper, GrantedAuthorityMapper grantedAuthorityMapper) {
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.userRoleCacheService = userRoleCacheService;
        this.userDetailsMapper = userDetailsMapper;
        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Logger.info("!!! CREATE UserDetails (by username) !!!");
        AppUser user = appUserService.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + "' was not found..."));

        return this.createUserDetails(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        // находим пользователя в кэше
        return userDetailsCacheService.getUserDetails(id.toString())
                .map(user -> {
                    Logger.info("UserDetails from Redis cache");
                    return user;
                })
                .orElseGet(() -> {
                    Logger.info("!!! CREATE UserDetails (by ID) !!!");
                    AppUser user = appUserService.findById(id)
                            .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));
                    return createUserDetails(user);
                });
    }

    public void addUserToCache(final Integer id, final UserDetails userDetails) {
        userDetailsCacheService.addToCache(id.toString(), userDetails);
    }

    public boolean checkUserInCache(final Integer id) {
        return userDetailsCacheService.isHave(id.toString());
    }

    private UserDetails createUserDetails(final AppUser user) {
        // установка ролей для данного пользователя
        List<GrantedAuthority> grantList = this.setRolesForUser(user.getId());

        // возвращаем объект внутреннего Spring UserDetails
        return userDetailsMapper.toCustomUserDetails(user, grantList);
    }

    private List<GrantedAuthority> setRolesForUser(final Integer id) {
        String userId = id.toString();

        // находим роли пользователя в кэше
        return userRoleCacheService.getRoles(userId)
                .map(authority -> {
                    Logger.info("UserRoles from Redis cache");
                    return grantedAuthorityMapper.toGrantedAuthorities(authority);
                })
                .orElseGet(() -> {
                    Logger.info("!!! CREATE UserRoles !!!");

                    // установка ролей для пользователя
                    List<String> roleNames = userRoleService.getRoleNames(id);
                    List<GrantedAuthority> authorities = grantedAuthorityMapper.toGrantedAuthorities(roleNames);

                    // записываем роли в кэш
                    userRoleCacheService.addToCache(userId, authorities);

                    return authorities;
                });
    }
}
