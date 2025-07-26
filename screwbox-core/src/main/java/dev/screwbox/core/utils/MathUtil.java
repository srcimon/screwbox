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
     *
     * @since 3.4.0
     */
    public static Random createRandomUsingMultipleSeeds(final long... seeds) {
        long combinedSeed = 29;
        for (final var seed : seeds) {
            long mixed = combinedSeed ^ seed;
            mixed ^= mixed << 17;
            mixed ^= mixed >>> 31;
            mixed ^= mixed << 8;
            combinedSeed += mixed;
        }
        return new Random(combinedSeed);
    }

    /**
     * Snaps a value to the nearest value in number grid. E.g snap(12, 8) will result in 8.
     *
     * @since 3.4.0
     */
    public static double snapToGrid(double value, int gridSize) {
        Validate.positive(gridSize, "grid size must be positive");
        return Math.floorDiv((int) value, gridSize) * (double)gridSize;
    }
}
