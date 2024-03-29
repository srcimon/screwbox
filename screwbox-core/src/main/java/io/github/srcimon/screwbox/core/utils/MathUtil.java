package io.github.srcimon.screwbox.core.utils;

/**
 * Utility functions for math operations.
 */
public final class MathUtil {

    private MathUtil() {
    }

    /**
     * Returns {@code true} if both values have the same sign.
     */
    public static boolean sameSign(final double value, final double other) {
        return modifier(value) == modifier(other);
    }

    /**
     * Returns 1 if value is positive or -1 if value is negative.
     */
    public static double modifier(final double value) {
        return value >= 0 ? 1 : -1;
    }

}
