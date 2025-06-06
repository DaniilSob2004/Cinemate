package com.example.cinemate.dao.contentactor;

import com.example.cinemate.model.db.ContentActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentActorRepository extends JpaRepository<ContentActor, Integer> {

    @Query("Select ca.actor.id from ContentActor ca where ca.content.id = ?1")
    List<Integer> getIdActors(Integer contentId);

    @Query("SELECT ca FROM ContentActor ca WHERE ca.content.id IN :ids")
    List<ContentActor> findAllByContentIds(@Param("ids") List<Integer> ids);

    void deleteByContentIdAndActorId(Integer contentId, Integer actorId);
}
