package com.example.cinemate.service.business.warning;

import com.example.cinemate.dto.warning.WarningDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.mapper.WarningMapper;
import com.example.cinemate.service.business_db.warningservice.WarningService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarningCrudService {

    private final WarningService warningService;
    private final WarningMapper warningMapper;

    public WarningCrudService(WarningService warningService, WarningMapper warningMapper) {
        this.warningService = warningService;
        this.warningMapper = warningMapper;
    }

    public List<WarningDto> getAll() {
        return warningService.findAll().stream()
                .map(warningMapper::toWarningDto)
                .toList();
    }

    public void add(final WarningDto warningDto) {
        warningDto.setName(warningDto.getName().toLowerCase());

        warningService.findByName(warningDto.getName())
                .ifPresent(content -> {
                    throw new ContentAlreadyExists("Warning '" + warningDto.getName() + "' already exists");
                });

        warningService.save(warningMapper.toWarningDto(warningDto));
    }
}
