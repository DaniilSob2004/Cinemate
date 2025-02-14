package com.example.cinemate.service.userdetail;

import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        // находим пользователя
        AppUser user = appUserService.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + "' was not found..."));

        // установка ролей для данного пользователя
        List<GrantedAuthority> grantList = this.setRolesForUser(user.getId());

        // возвращаем объект внутреннего Spring User
        return new User(user.getEmail(), user.getEncPassword(), grantList);
    }

    @Transactional
    public UserDetails loadUserByUser(AppUser user) {
        // установка ролей для данного пользователя
        List<GrantedAuthority> grantList = this.setRolesForUser(user.getId());

        // возвращаем объект внутреннего Spring User
        return new User(user.getEmail(), user.getEncPassword(), grantList);
    }

    private List<GrantedAuthority> setRolesForUser(final Integer id) {
        // установка ролей для пользователя
        List<String> roleNames = userRoleService.getRoleNames(id);
        return roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
