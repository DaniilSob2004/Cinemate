package com.example.cinemate.mapper.user;

import com.example.cinemate.dto.user.file.UserFilesBufferDto;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.mapper.common.TempContentFileMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserFileMapper {

    private final TempContentFileMapper tempContentFileMapper;

    public UserFileMapper(TempContentFileMapper tempContentFileMapper) {
        this.tempContentFileMapper = tempContentFileMapper;
    }

    // преобразования MultipartFile в UserFilesBufferDto (TempContentFile)
    public UserFilesBufferDto toUserFilesBufferDto(final UserFilesDto userFilesDto) {
        try {
            return new UserFilesBufferDto(
                    tempContentFileMapper.toTempContentFile(userFilesDto.getAvatar())
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to read multipart file: " + e.getMessage());
        }
    }
}
