package com.example.cinemate.dao.contentgenre;

import com.example.cinemate.model.db.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentGenreRepository extends JpaRepository<ContentGenre, Integer> {

}
