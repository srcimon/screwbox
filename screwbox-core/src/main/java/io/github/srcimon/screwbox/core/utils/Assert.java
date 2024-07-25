package io.github.srcimon.screwbox.core.utils;

//TODO javadoc changelog and USE USE USE
public class Assert {

    public static void isPositive(final int value, final String message) {
        if (value < 1) {
            throw new IllegalArgumentException(message);
        }
    }
}
