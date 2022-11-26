package de.suzufa.screwbox.core.utils;

public final class MathUtil {

    private MathUtil() {
    }

    public static double clamp(final double min, final double value, final double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean sameSign(final double value, final double other) {
        return value >= 0 == other >= 0;
    }

    public static double modifier(final double value) {
        return value >= 0 ? 1 : -1;
    }

}
