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
     * Creates a {@link Random} using multiple seeds. Will create reproducible seed when feed with same seeds.
     * Seed will differ when seeds are provided in distinct order.
     */
    //TODO changelog
    //TODO Test
    public static Random createRandomUsingMultipleSeeds(final long... seeds) {
        long combinedSeed = 29;
        for (final var seed : seeds) {
            combinedSeed += new Random(seed * 31 + combinedSeed * 29).nextLong();
        }
        return new Random(combinedSeed);
    }

}
