package com.example.cinemate.service.business_db.appuserservice;

import com.example.cinemate.dao.appuser.AppUserRepository;
import com.example.cinemate.model.db.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void save(AppUser user) {
        appUserRepository.save(user);
    }

    @Override
    public void saveUsersList(List<AppUser> users) {
        appUserRepository.saveAll(users);
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
}
