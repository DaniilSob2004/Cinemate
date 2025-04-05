package com.example.cinemate.mapper;

import com.example.cinemate.dto.warning.WarningDto;
import com.example.cinemate.model.db.Warning;
import org.springframework.stereotype.Component;

@Component
public class WarningMapper {

    public WarningDto toWarningDto(final Warning warning) {
        return new WarningDto(
                warning.getId(),
                warning.getName()
        );
    }

    public Warning toWarningDto(final WarningDto warningDto) {
        return new Warning(
                warningDto.getId(),
                warningDto.getName()
        );
    }
}
