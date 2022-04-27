package de.suzufa.screwbox.core.utils;

public final class MathUtil {

    private MathUtil() {
    }

    public static double clamp(final double min, final double value, final double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(final float min, final float value, final float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean haveSameSign(final double value, final double other) {
        return value >= 0 == other >= 0;
    }

    public static double modifier(final double value) {
        return value >= 0 ? 1 : -1;
    }

}
