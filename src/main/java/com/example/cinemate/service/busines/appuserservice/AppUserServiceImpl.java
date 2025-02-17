package com.example.cinemate.service.busines.appuserservice;

import com.example.cinemate.dao.appuser.AppUserRepository;
import com.example.cinemate.model.db.AppUser;
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

        // загружаем роли пользователя
        users.forEach(user -> user.setUserRoles(
                userRoleService.getRoleNames(user.getId()))
        );

        return Optional.of(users);
    }

    @Override
    public Optional<AppUser> findByEmailWithRoles(String email) {
        AppUser user = appUserRepository.findAppUserByEmail(email).orElse(null);
        return this.setUserRoles(user);  // загружаем роли пользователя
    }

    @Override
    public Optional<AppUser> findByIdWithRoles(Integer id) {
        AppUser user = appUserRepository.findAppUserById(id).orElse(null);
        return this.setUserRoles(user);  // загружаем роли пользователя
    }


    private Optional<AppUser> setUserRoles(AppUser user) {
        if (user != null) {
            user.setUserRoles(userRoleService.getRoleNames(user.getId()));
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
