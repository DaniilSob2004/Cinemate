package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class GenerateUtil {

    private static final Random RAND = new Random();
    private static final int MIN_INT = 1;
    private static final int MAX_INT = 100_000_000;
    private static final int SIZE_NUM_TEL = 12;

    public static String getEmailByName(final String name) {
        int num = getRandomInteger(1000, false);
        int num2 = getRandomInteger(1000, false);

        StringBuilder sb = new StringBuilder();
        sb.append(name)
                .append("_")
                .append(num)
                .append("_")
                .append(num2)
                .append("@gmail.com");

        return sb.toString();
    }

    public static String getRandomNumTel() {
        StringBuilder sb = new StringBuilder("+");
        for (int i = 0; i < SIZE_NUM_TEL; i++) {
            sb.append(getRandomInteger(1, 9));
        }
        return sb.toString();
    }

    public static String getRandomNumberString() {
        int size = 30;
        return IntStream.range(0, size)
                .map(i -> getRandomInteger(1, 10))
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }

    public static String getRandomTokenString() {
        int size = 30;
        return IntStream.range(0, size)
                .map(i -> RAND.nextInt(26) + 'a')
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
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
