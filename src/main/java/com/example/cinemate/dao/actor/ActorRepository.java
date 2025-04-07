package com.example.cinemate.dao.actor;

import com.example.cinemate.model.db.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Optional<Actor> findActorByNameAndSurname(String name, String surname);
}
