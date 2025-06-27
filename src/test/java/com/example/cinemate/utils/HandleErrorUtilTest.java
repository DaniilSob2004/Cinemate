package com.example.cinemate.utils;

import com.example.cinemate.dto.error.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class HandleErrorUtilTest {

    @Test
    void handleUserInactiveException_ShouldReturnLockedForInactiveUser() {
        Exception e = new Exception("User account is inactive");
        ErrorResponseDto response = HandleErrorUtil.handleUserInactiveException(e);

        assertEquals("User is inactive...", response.getMessage());
        assertEquals(HttpStatus.LOCKED.value(), response.getStatus());
    }

    @Test
    void handleUserInactiveException_ShouldReturnUnauthorizedForOtherMessages() {
        Exception e = new Exception("Invalid credentials");
        ErrorResponseDto response = HandleErrorUtil.handleUserInactiveException(e);

        assertEquals("Invalid credentials", response.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    void handleUserInactiveException_ShouldBeCaseSensitive() {
        Exception e = new Exception("Account is INACTIVE USER");
        ErrorResponseDto response = HandleErrorUtil.handleUserInactiveException(e);

        assertEquals("User is inactive...", response.getMessage());
        assertEquals(HttpStatus.LOCKED.value(), response.getStatus());
    }

    @Test
    void handleUserInactiveException_ShouldHandleNullMessageGracefully() {
        Exception e = new Exception((String) null);
        assertThrows(NullPointerException.class, () -> HandleErrorUtil.handleUserInactiveException(e));
    }
}