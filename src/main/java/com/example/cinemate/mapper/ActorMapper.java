package com.example.cinemate.mapper;

import com.example.cinemate.dto.actor.ActorDto;
import com.example.cinemate.model.db.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorDto toActorDto(final Actor actor) {
        return new ActorDto(
                actor.getId(),
                actor.getName(),
                actor.getSurname(),
                actor.getBiography()
        );
    }

    public Actor toActor(final ActorDto actorDto) {
        return new Actor(
                null,
                actorDto.getName(),
                actorDto.getSurname(),
                actorDto.getBiography()
        );
    }
}
