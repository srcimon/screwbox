package dev.screwbox.core.utils;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;

import static java.lang.Math.abs;

/**
 * An implementation of the Perlin noise algorithm for creating reproducible noise values.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Perlin_noise">Wikipedia Perlin Noise</a>
 * @since 3.4.0
 */
public final class PerlinNoise {

    private PerlinNoise() {
    }

    /**
     * Create noise for the specified 3d coordinate using the specified seed.
     *
     * @return perlin noise value in the range of -1 to 1.
     * @since 3.6.0
     */
    public static double generatePerlinNoise3d(final long seed, final double x, final double y, final double z) {
        final var topLeft = NoiseNode3D.createAt(abs(x), abs(y), abs(z));
        final var topRight = topLeft.nextNode(1, 0, 0);
        final var lowerLeft = topLeft.nextNode(0, 1, 0);
        final var lowerRight = topLeft.nextNode(1, 1, 0);

        final var topLeftUpper = topLeft.nextNode(0, 0, 1);
        final var topRightUpper = topLeft.nextNode(1, 0, 1);
        final var lowerLeftUpper = topLeft.nextNode(0, 1, 1);
        final var lowerRightUpper = topLeft.nextNode(1, 1, 1);

        final Percent deltaZ = Ease.S_CURVE_IN.applyOn(Percent.of(topLeft.deltaZ));
        final double a = deltaZ.rangeValue(topLeft.gradientValue(seed), topLeftUpper.gradientValue(seed));
        final double b = deltaZ.rangeValue(topRight.gradientValue(seed), topRightUpper.gradientValue(seed));
        final double c = deltaZ.rangeValue(lowerLeft.gradientValue(seed), lowerLeftUpper.gradientValue(seed));
        final double d = deltaZ.rangeValue(lowerRight.gradientValue(seed), lowerRightUpper.gradientValue(seed));

        final Percent deltaX = Ease.S_CURVE_IN.applyOn(Percent.of(topLeft.deltaX));
        final double upperGradient = deltaX.rangeValue(a, b);
        final double lowerGradient = deltaX.rangeValue(c, d);

        final Percent deltaY = Ease.S_CURVE_IN.applyOn(Percent.of(topLeft.deltaY));
        return Math.clamp(deltaY.rangeValue(upperGradient, lowerGradient), -1, 1);
    }

    /**
     * Create noise for the specified position using the specified seed.
     *
     * @return perlin noise value in the range of -1 to 1.
     */
    public static double generatePerlinNoise(final long seed, final double x, final double y) {
        final var topLeft = NoiseNode.createAt(abs(x), abs(y));
        final var topRight = topLeft.nextNode(1, 0);
        final var lowerLeft = topLeft.nextNode(0, 1);
        final var lowerRight = topLeft.nextNode(1, 1);

        final Percent deltaX = Ease.S_CURVE_IN.applyOn(Percent.of(topLeft.deltaX));
        final double upperGradient = deltaX.rangeValue(topLeft.gradientValue(seed), topRight.gradientValue(seed));
        final double lowerGradient = deltaX.rangeValue(lowerLeft.gradientValue(seed), lowerRight.gradientValue(seed));

        final Percent deltaY = Ease.S_CURVE_IN.applyOn(Percent.of(topLeft.deltaY));
        return deltaY.rangeValue(upperGradient, lowerGradient);
    }

    private record NoiseNode(int x, int y, double deltaX, double deltaY) {

        private static NoiseNode createAt(final double x, final double y) {
            final double floorX = Math.floor(x);
            final double floorY = Math.floor(y);
            final int offsetX = ((int) floorX & 255);
            final int offsetY = ((int) floorY & 255);
            return new NoiseNode(offsetX, offsetY, x - floorX, y - floorY);
        }

        private NoiseNode nextNode(final int distanceX, final int distanceY) {
            return new NoiseNode(x + distanceX, y + distanceY, deltaX - distanceX, deltaY - distanceY);
        }

        private double gradientValue(final long seed) {
            final var random = MathUtil.createRandomUsingMultipleSeeds(seed, x, y);
            return (random.nextBoolean() ? 1 : -1) * deltaX + (random.nextBoolean() ? 1 : -1) * deltaY;
        }
    }

    private record NoiseNode3D(int x, int y, int z, double deltaX, double deltaY, double deltaZ) {

        private static NoiseNode3D createAt(final double x, final double y, final double z) {
            final double floorX = Math.floor(x);
            final double floorY = Math.floor(y);
            final double floorZ = Math.floor(z);
            final int offsetX = ((int) floorX & 255);
            final int offsetY = ((int) floorY & 255);
            final int offsetZ = ((int) floorZ & 255);
            return new NoiseNode3D(offsetX, offsetY, offsetZ, x - floorX, y - floorY, z - floorZ);
        }

        private NoiseNode3D nextNode(final int distanceX, final int distanceY, final int distanceZ) {
            return new NoiseNode3D(x + distanceX, y + distanceY, z + distanceZ, deltaX - distanceX, deltaY - distanceY, deltaZ - distanceZ);
        }

        private double gradientValue(final long seed) {
            final var random = MathUtil.createRandomUsingMultipleSeeds(seed, x, y, z);
            return (random.nextBoolean() ? 1 : -1) * deltaX + (random.nextBoolean() ? 1 : -1) * deltaY + (random.nextBoolean() ? 1 : -1) * deltaZ;
        }
    }
}
