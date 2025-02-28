package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}

