package com.example.cinemate.dao.appuser;

import com.example.cinemate.model.db.AppUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AppUserRepository extends PagingAndSortingRepository<AppUser, Integer>, JpaSpecificationExecutor<AppUser> {
    boolean existsAppUserByEmail(String email);
    boolean existsAppUsersById(Integer id);
    Optional<AppUser> findAppUserById(Integer id);
    Optional<AppUser> findAppUserByEmail(String email);
    Optional<AppUser> findByEmailAndIsActiveTrue(String email);
    Optional<AppUser> findByIdAndIsActiveTrue(Integer id);
}
