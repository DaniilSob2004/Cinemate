package com.example.cinemate.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class CinemateInitializer {

    @Autowired
    private CinemateDbInitializer cinemateDbInitializer;

    public void autoBaseInitialize() {
        try {
            //cinemateDbInitializer.deleteTables();
            //cinemateDbInitializer.deleteAllRowsInDB();
            //cinemateDbInitializer.createAppUsers();
            //cinemateDbInitializer.createExternalAuth();
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }
    }
}
