package com.example.cinemate.service.business.actor;

import com.example.cinemate.dto.actor.ActorDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.mapper.ActorMapper;
import com.example.cinemate.service.business_db.actorservice.ActorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorCrudService {

    private final ActorService actorService;
    private final ActorMapper actorMapper;

    public ActorCrudService(ActorService actorService, ActorMapper actorMapper) {
        this.actorService = actorService;
        this.actorMapper = actorMapper;
    }

    public List<ActorDto> getAll() {
        return actorService.findAll().stream()
                .map(actorMapper::toActorDto)
                .toList();
    }

    public void add(final ActorDto actorDto) {
        actorDto.setName(actorDto.getName().toLowerCase());
        actorDto.setSurname(actorDto.getSurname().toLowerCase());

        actorService.findByNameAndSurname(actorDto.getName(), actorDto.getSurname())
                .ifPresent(actor -> {
                    throw new ContentAlreadyExists("Actor '" + actorDto.getName() + " " + actorDto.getSurname() + "' already exists");
                });

        actorService.save(actorMapper.toActor(actorDto));
    }
}
