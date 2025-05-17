package com.example.cinemate.dao.contentgenre;

import com.example.cinemate.model.db.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentGenreRepository extends JpaRepository<ContentGenre, Integer> {
    @Query("Select cg.genre.id from ContentGenre cg where cg.content.id = ?1")
    List<Integer> getIdGenres(Integer contentId);

    @Query("SELECT cg FROM ContentGenre cg WHERE cg.content.id IN :ids")
    List<ContentGenre> findAllByContentIds(@Param("ids") List<Integer> ids);

    void deleteByContentIdAndGenreId(Integer contentId, Integer genreId);
}
