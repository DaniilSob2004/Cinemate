package com.example.cinemate.dao.userrole;

import com.example.cinemate.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("Select ur.role.name from UserRole ur where ur.user.id = ?1")
    List<String> getRoleNames(Integer userId);
}
