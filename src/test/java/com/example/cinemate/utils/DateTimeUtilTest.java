package com.example.cinemate.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilTest {

    @Test
    void calculateTtl_ShouldReturnPositiveSeconds() {
        Date future = new Date(System.currentTimeMillis() + 10_000);
        long ttl = DateTimeUtil.calculateTtl(future);
        assertTrue(ttl >= 8 && ttl <= 10);
    }

    @Test
    void calculateTtl_ShouldReturnNegativeIfExpired() {
        Date past = new Date(System.currentTimeMillis() - 5000);
        long ttl = DateTimeUtil.calculateTtl(past);
        assertTrue(ttl < 0);
    }

    @Test
    void formatDate_ShouldReturnCorrectFormat() {
        LocalDate date = LocalDate.of(2024, 12, 5);
        String formatted = DateTimeUtil.formatDate(date);
        assertEquals("2024-12-05", formatted);
    }

    @Test
    void formatDateTime_ShouldReturnCorrectFormat() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 2, 14, 30, 45);
        String formatted = DateTimeUtil.formatDateTime(dateTime);
        assertEquals("2025-01-02 14:30:45", formatted);
    }

    @Test
    void isDateAfterNow_ShouldReturnTrueIfFutureDate() {
        String future = LocalDate.now().plusDays(1).toString();
        assertTrue(DateTimeUtil.isDateAfterNow(future));
    }

    @Test
    void isDateAfterNow_ShouldReturnFalseIfTodayOrPast() {
        String today = LocalDate.now().toString();
        String yesterday = LocalDate.now().minusDays(1).toString();

        assertFalse(DateTimeUtil.isDateAfterNow(today));
        assertFalse(DateTimeUtil.isDateAfterNow(yesterday));
    }

    @Test
    void isDateAfterNow_ShouldReturnTrueIfInvalidDate() {
        assertTrue(DateTimeUtil.isDateAfterNow("not-a-date"));
        assertTrue(DateTimeUtil.isDateAfterNow("2025-13-99"));
    }
}