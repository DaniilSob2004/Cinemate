package com.example.cinemate.dao.externalauth;

import com.example.cinemate.model.ExternalAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
public interface ExternalAuthRepository extends JpaRepository<ExternalAuth, Integer> {

}
