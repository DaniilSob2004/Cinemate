package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public static String capitalizeFirstLetter(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String getUsernameFromEmail(final String email) {
        String username = "user";

        if (email != null && !email.isEmpty()) {
            int ind = email.indexOf("@");
            if (ind > 0) {
                username = email.substring(0, ind);
            }
        }

        return username;
    }

    public static String addSymbolInStart(final String str, final String symbol) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return symbol + str;
    }

    public static String getFirstLetter(final String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1);
    }
}

