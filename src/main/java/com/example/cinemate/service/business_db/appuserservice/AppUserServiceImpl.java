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
    public boolean existsByEmail(String email) {
        return appUserRepository.existsAppUserByEmail(email);
    }

    @Override
    public Optional<AppUser> findByIdWithoutIsActive(Integer id) {
        return appUserRepository.findAppUserById(id);
    }

    @Override
    public Optional<AppUser> findByEmailWithoutIsActive(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmailAndIsActiveTrue(email);
    }

    @Override
    public Optional<AppUser> findById(Integer id) {
        return appUserRepository.findByIdAndIsActiveTrue(id);
    }
}
