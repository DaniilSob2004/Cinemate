package com.example.cinemate.dao.appuser;

import com.example.cinemate.model.db.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsAppUserByEmail(String email);
    Optional<AppUser> findAppUserById(Integer id);
    Optional<AppUser> findAppUserByEmail(String email);
    Optional<AppUser> findByEmailAndIsActiveTrue(String email);
    Optional<AppUser> findByIdAndIsActiveTrue(Integer id);
}
