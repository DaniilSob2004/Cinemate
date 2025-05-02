package com.example.cinemate.dao.content;

import com.example.cinemate.model.db.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    Optional<Content> findContentById(Integer id);
    Optional<Content> findContentByNameIgnoreCase(String name);
}
