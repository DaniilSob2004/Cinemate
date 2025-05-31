package com.example.cinemate.dao.episode;

import com.example.cinemate.model.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    List<Episode> getEpisodesByContentId(Integer contentId);
}
