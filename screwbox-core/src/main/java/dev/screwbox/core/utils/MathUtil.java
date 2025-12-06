package dev.screwbox.core.utils;

/**
 * Utility functions for math operations.
 */
public final class MathUtil {

    private static final int LOOKUP_RANGE = 65536;
    private static final double LOOKUP_MULTIPLIER = LOOKUP_RANGE / (2.0 * Math.PI);
    private static final double[] SIN_LOOKUP_TABLE = new double[LOOKUP_RANGE];
    private static final double[] COS_LOOKUP_TABLE = new double[LOOKUP_RANGE];

    static {
        for (int i = 0; i < LOOKUP_RANGE; ++i) {
            final double value = i * Math.PI * 2.0 / LOOKUP_RANGE;
            SIN_LOOKUP_TABLE[i] = Math.sin(value);
            COS_LOOKUP_TABLE[i] = Math.cos(value);
        }
    }

    private MathUtil() {
    }

    /**
     * Returns {@code true} if both values have the same sign.
     */
    public static boolean sameSign(final double value, final double other) {
        return value >= 0 == other >= 0;
    }

    /**
     * Returns 1 if value is positive or -1 if value is negative.
     */
    public static double modifier(final double value) {
        return value >= 0 ? 1 : -1;
    }

    /**
     * Creates a combined seed using multiple input seeds. Will create reproducible seed when feed with same seeds.
     *
     * @since 3.6.0
     */
    public static long combineSeeds(final long... seeds) {
        long combinedSeed = 29;
        for (final var seed : seeds) {
            long mixed = combinedSeed ^ seed;
            mixed ^= mixed << 17;
            mixed ^= mixed >>> 31;
            mixed ^= mixed << 8;
            combinedSeed += mixed;
        }
        return combinedSeed;
    }

    /**
     * Snaps a value to the nearest value in number grid. E.g snap(12, 8) will result in 8.
     *
     * @since 3.4.0
     */
    public static double snapToGrid(final double value, final int gridSize) {
        Validate.positive(gridSize, "grid size must be positive");
        return Math.floorDiv((int) value, gridSize) * (double) gridSize;
    }

    /**
     * Calculates sinus less precise than {@link Math#sin(double)} but is up to 3 times faster.
     *
     * @since 3.15.0
     */
    public static double fastSin(final double value) {
        return SIN_LOOKUP_TABLE[(int) (value * LOOKUP_MULTIPLIER) & 0xFFFF];
    }

    /**
     * Calculates cosinus less precise than {@link Math#cos(double)} but is up to 3 times faster.
     *
     * @since 3.15.0
     */
    public static double fastCos(final double value) {
        return COS_LOOKUP_TABLE[(int) (value * LOOKUP_MULTIPLIER) & 0xFFFF];
    }

    //TODO Document
    //TODO changelog
    public static boolean isEven(final int value) {
        return value % 2 == 0;
    }

    //TODO Document
    //TODO changelog
    public static boolean isUneven(final int value) {
       return !isEven(value);
    }
}
