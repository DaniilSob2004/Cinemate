package com.example.cinemate.service.business_db.appuserservice;

import com.example.cinemate.dao.appuser.AppUserRepository;
import com.example.cinemate.dto.user.UserSearchParamsDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public void deleteAll() {
        appUserRepository.deleteAll();
    }


    @Override
    public Page<AppUser> getUsers(UserSearchParamsDto userSearchParamsDto) {
        Pageable pageable = PaginationUtil.getPageable(userSearchParamsDto);
        Specification<AppUser> specAppUsers = Specification.where(this.searchSpecification(userSearchParamsDto.getSearchStr()));
        return appUserRepository.findAll(specAppUsers, pageable);
    }

    private Specification<AppUser> searchSpecification(final String queryStr) {
        // Specification<AppUser> - описание условия (WHERE), которое можно применить в findAll()
        // root — корневая сущность (AppUser)
        // cb — (CriteriaBuilder), фабрика для создания SQL-выражений (LIKE, AND, OR и т.д.)
        return (root, cq, cb) -> {
            if (queryStr == null || queryStr.isEmpty()) {
                return cb.conjunction();  // никаких условий нет, вернутся все записи
            }
            String pattern = "%" + queryStr.toLowerCase() + "%";
            return cb.or(  // возвращаем OR между условиями
                    cb.like(cb.lower(root.get("firstname")), pattern),
                    cb.like(cb.lower(root.get("surname")), pattern),
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
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
