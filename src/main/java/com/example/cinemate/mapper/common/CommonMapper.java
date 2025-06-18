package com.example.cinemate.mapper.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Component
public class CommonMapper {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public CommonMapper(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public <T> T toDtoAndValidation(final String metadataStr, final Class<T> classType) throws JsonProcessingException {
        // получение dto и проверка валидности
        var dto = objectMapper.readValue(metadataStr, classType);
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return dto;
    }
}
