package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;
import java.util.Random;

@UtilityClass
public class GenerateUtil {

    private static final Random RAND = new Random();
    private static final int MIN_INT = 1;
    private static final int MAX_INT = 100_000_000;
    private static final int SIZE_NUM_TEL = 12;

    public static String getEmailByName(final String name) {
        return name + "@gmail.com";
    }

    public static String getRandomNumTel() {
        StringBuilder sb = new StringBuilder("+");
        for (int i = 0; i < SIZE_NUM_TEL; i++) {
            sb.append(getRandomInteger(1, 9));
        }
        return sb.toString();
    }

    public static double getRandomDouble(final double min, final double max) {
        return RAND.nextDouble(min, max);
    }

    public static int getRandomInteger() {
        return getRandomInteger(MIN_INT, MAX_INT);
    }

    public static int getRandomInteger(final int value, final boolean isMaxValue) {
        if (isMaxValue) {
            return getRandomInteger(MIN_INT, value);
        }
        return getRandomInteger(value, MAX_INT);
    }

    public static int getRandomInteger(final int min, final int max) {
        return RAND.nextInt(min, max);
    }
}
