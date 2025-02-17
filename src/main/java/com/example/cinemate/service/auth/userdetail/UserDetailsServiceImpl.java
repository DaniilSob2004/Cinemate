package com.example.cinemate.service.auth.userdetail;

import com.example.cinemate.convert.UserDetailsConvertDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserDetailsCacheService userDetailsCacheService;

    @Autowired
    private UserDetailsConvertDto userDetailsConvertDto;

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
        UserDetails userDetailsFromCache = userDetailsCacheService.get(id.toString()).orElse(null);
        if (userDetailsFromCache != null) {
            Logger.info("UserDetails from Redis cache");
            return userDetailsFromCache;
        }

        Logger.info("!!! CREATE UserDetails (by ID) !!!");
        AppUser user = appUserService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        return this.createUserDetails(user);
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
        return userDetailsConvertDto.convertToCustomUserDetails(user, grantList);
    }

    private List<GrantedAuthority> setRolesForUser(final Integer id) {
        // TODO: доставать из кэша, если нет, то создать и добавить в кэш

        // установка ролей для пользователя
        List<String> roleNames = userRoleService.getRoleNames(id);
        return roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
