package com.example.cinemate.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CinemateInitializer {

    @Autowired
    private CinemateDbInitializer cinemateDbInitializer;

    public void autoBaseInitialize() {
        //cinemateDbInitializer.deleteAllRowsInDB();
        //cinemateDbInitializer.createAppUsers();
        //cinemateDbInitializer.createExternalAuth();
    }
}
