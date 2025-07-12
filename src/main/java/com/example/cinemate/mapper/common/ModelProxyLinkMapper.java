package com.example.cinemate.mapper.common;

import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.Content;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

// не загружаем модельки из БД, а создаём прокси-ссылку
@Component
public class ModelProxyLinkMapper {

    private final EntityManager entityManager;

    public ModelProxyLinkMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Content getContentById(final int id) {
        Content contentRef;
        try {
            contentRef = entityManager.getReference(Content.class, id);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("Content with id " + id + " not found");
        }
        return contentRef;
    }

    public AppUser getAppUserById(final int id) {
        AppUser appUserRef;
        try {
            appUserRef = entityManager.getReference(AppUser.class, id);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("AppUser with id " + id + " not found");
        }
        return appUserRef;
    }
}
