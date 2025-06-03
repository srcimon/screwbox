package dev.screwbox.core.utils;

import dev.screwbox.core.Ease;
import dev.screwbox.core.Percent;
import dev.screwbox.core.utils.MathUtil;

import java.util.Random;

/**
 * See <a href="https://en.wikipedia.org/wiki/Perlin_noise">Wikipedia Perlin Noise</a>
 */
//TODO Document
    //TODO Test
    //TODO changelog
public class PerlinNoise {

    //TODO Perlin noise below
    public static double generatePerlinNoise(long seed, double x, double y) {
        final var topLeft = NoiseNode.createAt(x, y);
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

        public static NoiseNode createAt(double x, double y) {
            final double floorX = Math.floor(x);
            final double floorY = Math.floor(y);
            final int offsetX = ((int) floorX & 255);
            final int offsetY = ((int) floorY & 255);
            return new NoiseNode(offsetX, offsetY, x - floorX, y - floorY);
        }

        public NoiseNode nextNode(int dX, int dY) {
            return new NoiseNode(x + dX, y + dY, deltaX - dX, deltaY - dY);
        }

        double gradientValue(long seed) {
            Random random = MathUtil.createRandomUsingMultipleSeeds(seed, x, y);
            return (random.nextBoolean() ? 1 : -1) * deltaX + (random.nextBoolean() ? 1 : -1) * deltaY;
        }
    }
}
