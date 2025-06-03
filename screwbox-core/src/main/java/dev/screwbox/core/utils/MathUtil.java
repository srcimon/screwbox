package dev.screwbox.core.utils;

import java.util.Random;

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

    /**
     * Creates a {@link Random} using three seeds.
     */
    public static Random createRandomUsingMultipleSeeds(final long seedA, final long seedB, final long seedC) {
        final long componentA = new Random(seedA * 29L).nextLong();
        final long componentB = new Random(seedB * 33L).nextLong();
        final long componentC = new Random(seedC * 31L).nextLong();
        return new Random(componentC + componentB + componentA);
    }
}
