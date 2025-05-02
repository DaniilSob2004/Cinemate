package com.example.cinemate.validate.common;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
public class CommonDataValidate {

    public String getIsFieldExists(final String sortBy, final Field[] fields) {
        String defaultSortBy = "id";

        if (sortBy == null || sortBy.trim().isEmpty()) {
            return defaultSortBy;
        }

        boolean fieldExists = Arrays.stream(fields)
                .anyMatch(field -> field.getName().equals(sortBy));

        return fieldExists ? sortBy : defaultSortBy;
    }
}
