package com.example.cinemate.dao.episode;

import com.example.cinemate.model.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    Optional<Episode> findEpisodeById(Integer id);
    List<Episode> getEpisodesByContentId(Integer contentId);
    boolean existsEpisodeByContentIdAndSeasonNumberAndEpisodeNumber(Integer contentId, int seasonNumber, int episodeNumber);

    @Query("SELECT e.id FROM Episode e WHERE e.content.id = :contentId AND e.seasonNumber = :seasonNumber AND e.episodeNumber = :episodeNumber")
    Optional<Integer> getIdByContentIdAndSeasonNumberAndEpisodeNumber(
            @Param("contentId") Integer contentId,
            @Param("seasonNumber") int seasonNumber,
            @Param("episodeNumber") int episodeNumber
    );
}
