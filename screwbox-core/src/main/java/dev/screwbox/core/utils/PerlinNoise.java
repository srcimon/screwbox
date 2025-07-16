package dev.screwbox.core.utils;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;

import java.util.Random;

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

    //TODO NEW!!!!
    public static double generatePerlinNoise(final long seed, final double x, final double y, final double z) {
        final int floorZ = (int) Math.floor(z);
        final double distanceFloorZ = z - floorZ;
        final var noiseA = generatePerlinNoise(seed + floorZ, x, y);
        final var noiseB = generatePerlinNoise(seed + floorZ + 1, x, y);
        return noiseA * (1 - distanceFloorZ ) + noiseB * (distanceFloorZ);
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

        public static NoiseNode createAt(final double x, final double y) {
            final double floorX = Math.floor(x);
            final double floorY = Math.floor(y);
            final int offsetX = ((int) floorX & 255);
            final int offsetY = ((int) floorY & 255);
            return new NoiseNode(offsetX, offsetY, x - floorX, y - floorY);
        }

        public NoiseNode nextNode(final int distanceX, final int distanceY) {
            return new NoiseNode(x + distanceX, y + distanceY, deltaX - distanceX, deltaY - distanceY);
        }

        double gradientValue(final long seed) {
            Random random = MathUtil.createRandomUsingMultipleSeeds(seed, x, y);
            return (random.nextBoolean() ? 1 : -1) * deltaX + (random.nextBoolean() ? 1 : -1) * deltaY;
        }
    }
}
