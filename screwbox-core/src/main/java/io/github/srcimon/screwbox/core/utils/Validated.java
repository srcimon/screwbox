package io.github.srcimon.screwbox.core.utils;

//TODO javadoc changelog and USE USE USE
public final class Validated {

    private Validated() {
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

}
