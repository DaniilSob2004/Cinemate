package com.example.cinemate.service.db;

import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.util.Arrays;

@Service
public class CinemateInitializer {

    private final CinemateDbInitializer cinemateDbInitializer;

    public CinemateInitializer(CinemateDbInitializer cinemateDbInitializer) {
        this.cinemateDbInitializer = cinemateDbInitializer;
    }

    public void autoBaseInitialize() {
        try {
            //cinemateDbInitializer.deleteTables();
            /*cinemateDbInitializer.deleteAllRowsInDB();

            cinemateDbInitializer.createAppUsers();
            cinemateDbInitializer.createExternalAuth();
            cinemateDbInitializer.createContentTypes();
            cinemateDbInitializer.createWarnings();
            cinemateDbInitializer.createActors();
            cinemateDbInitializer.createGenres();
            cinemateDbInitializer.createContents();
            cinemateDbInitializer.createContentGenres();
            cinemateDbInitializer.createContentActors();
            cinemateDbInitializer.createContentWarnings();
            cinemateDbInitializer.createEpisodes();
            cinemateDbInitializer.createWishLists();
            cinemateDbInitializer.createContentViewHistories();*/
        }
        catch (Exception e) {
            Logger.error(e.getClass() + " - " + e.getMessage() + " - " + Arrays.toString(e.getStackTrace()));
        }
    }
}
