package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@UtilityClass
public class DateTimeUtil {

    public long calculateTtl(final Date expirationDate) {
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }

    public String formatDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String formatDateTime(final LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public boolean isDateAfterNow(final String checkDateStr) {
        try {
            var checkDate = LocalDate.parse(checkDateStr);
            var currDate = LocalDate.now();
            return checkDate.isAfter(currDate);
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}
