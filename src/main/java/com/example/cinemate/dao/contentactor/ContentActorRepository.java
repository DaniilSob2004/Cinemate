package com.example.cinemate.dao.contentactor;

import com.example.cinemate.model.db.ContentActor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentActorRepository extends JpaRepository<ContentActor, Integer> {

}
