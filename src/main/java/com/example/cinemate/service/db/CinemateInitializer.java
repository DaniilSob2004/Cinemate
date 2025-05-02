package com.example.cinemate.service.db;

import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class CinemateInitializer {

    private final CinemateDbInitializer cinemateDbInitializer;

    public CinemateInitializer(CinemateDbInitializer cinemateDbInitializer) {
        this.cinemateDbInitializer = cinemateDbInitializer;
    }

    public void autoBaseInitialize() {
        try {
            //cinemateDbInitializer.deleteTables();
            //cinemateDbInitializer.deleteAllRowsInDB();
            //cinemateDbInitializer.createAppUsers();
            //cinemateDbInitializer.createExternalAuth();
            //cinemateDbInitializer.createContentTypes();
            //cinemateDbInitializer.createWarnings();
            //cinemateDbInitializer.createActors();
            //cinemateDbInitializer.createGenres();
            //cinemateDbInitializer.createContents();
            //cinemateDbInitializer.createContentGenres();
            //cinemateDbInitializer.createContentActors();
            //cinemateDbInitializer.createContentWarnings();
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }
}
