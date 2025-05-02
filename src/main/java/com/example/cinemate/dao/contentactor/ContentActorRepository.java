package com.example.cinemate.dao.contentactor;

import com.example.cinemate.model.db.ContentActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentActorRepository extends JpaRepository<ContentActor, Integer> {

    @Query("Select ca.actor.id from ContentActor ca where ca.content.id = ?1")
    List<Integer> getIdActors(Integer contentId);

    void deleteByContentIdAndActorId(Integer contentId, Integer actorId);
}
