package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Date;

@UtilityClass
public class DateTimeUtil {

    public long calculateTtl(final Date expirationDate) {
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }

    public boolean isDateAfterNow(final String checkDateStr) {
        var checkDate = LocalDate.parse(checkDateStr);
        var currDate = LocalDate.now();
        return checkDate.isAfter(currDate);
    }
}
