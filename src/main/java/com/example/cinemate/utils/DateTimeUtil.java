package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DateTimeUtil {

    public long calculateTtl(final Date expirationDate) {
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }
}
