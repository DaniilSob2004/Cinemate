package com.example.cinemate.dao.contenttype;

import com.example.cinemate.model.db.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {
    Optional<ContentType> findContentTypeByName(String name);
    Optional<ContentType> findContentTypeById(Integer id);
}
