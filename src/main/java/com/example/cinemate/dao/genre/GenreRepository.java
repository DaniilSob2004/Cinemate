package com.example.cinemate.dao.genre;

import com.example.cinemate.model.db.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findGenreById(Integer id);
    Optional<Genre> findGenreByName(String name);
}
