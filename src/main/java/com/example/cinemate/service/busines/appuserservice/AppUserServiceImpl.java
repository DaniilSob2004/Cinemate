package com.example.cinemate.service.busines.appuserservice;

import com.example.cinemate.dao.appuser.AppUserRepository;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.redis.UserRoleCacheService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleCacheService userRoleCacheService;

    @Override
    public void save(AppUser user) {
        appUserRepository.save(user);
    }

    @Override
    public int[] saveUsersList(List<AppUser> users) {
        appUserRepository.saveAll(users);
        return new int[0];
    }

    @Override
    public void update(AppUser user) {
        appUserRepository.save(user);
    }

    @Override
    public void delete(AppUser user) {
        appUserRepository.delete(user);
    }

    @Override
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Override
    public void deleteAll() {
        appUserRepository.deleteAll();
    }


    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }

    @Override
    public Optional<AppUser> findById(Integer id) {
        return appUserRepository.findAppUserById(id);
    }

    @Override
    public Optional<List<AppUser>> findAllWithRoles() {
        List<AppUser> users = appUserRepository.findAll();
        users.forEach(this::setUserRoles);  // загружаем роли пользователя
        return users.isEmpty() ? Optional.empty() : Optional.of(users);
    }

    @Override
    public Optional<AppUser> findByEmailWithRoles(String email) {
        return appUserRepository.findAppUserByEmail(email)
                .flatMap(this::setUserRoles);  // загружаем роли пользователя
    }

    @Override
    public Optional<AppUser> findByIdWithRoles(Integer id) {
        return appUserRepository.findAppUserById(id)
                .flatMap(this::setUserRoles);  // загружаем роли пользователя
    }


    private Optional<AppUser> setUserRoles(AppUser user) {
        if (user == null) {
            return Optional.empty();
        }

        // находим роли пользователя в кэше, иначе ищем в БД
        List<String> roles = userRoleCacheService.getRoles(user.getId().toString())
                .orElseGet(() -> userRoleService.getRoleNames(user.getId()));

        user.setUserRoles(roles);
        return Optional.of(user);
    }
}
