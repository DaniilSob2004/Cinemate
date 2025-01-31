package com.example.cinemate.service.busines.appuserservice;

import com.example.cinemate.dao.appuser.AppUserRepository;
import com.example.cinemate.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

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
}
