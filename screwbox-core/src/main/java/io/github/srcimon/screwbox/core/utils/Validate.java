package io.github.srcimon.screwbox.core.utils;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.ListUtil.containsDuplicates;

//TODO javadoc changelog and USE USE USE
public final class Validate {

    private Validate() {
    }

    public static int positive(final int value, final String message) {
        if (value < 1) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double positive(final double value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int aboveZero(final int value, final String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T> void notEmpty(final List<T> list, final String message) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void noDuplicates(final List<T> list, final String message) {
        if (containsDuplicates(list)) {
            throw new IllegalArgumentException(message);
        }
    }
}
