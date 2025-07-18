package dev.screwbox.core.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PerlinNoiseTest {

    private static final Random RANDOM = new Random();

    @ParameterizedTest
    @MethodSource("random3dPerlinInputs")
    void generatePerlinNoise3d_randomInputs_generatesValidResult(long seed, double x, double y, double z) {
        var result = PerlinNoise.generatePerlinNoise3d(seed, x, y, z);
        assertThat(result).isBetween(-1.0, 1.0);
    }

    @ParameterizedTest
    @MethodSource("random2dPerlinInputs")
    void generatePerlinNoise_randomInputs_generatesValidResult(long seed, double x, double y) {
        var result = PerlinNoise.generatePerlinNoise(seed, x, y);
        assertThat(result).isBetween(-1.0, 1.0);
    }

    @ParameterizedTest
    @MethodSource("random3dPerlinInputs")
    void generatePerlinNoise3d_repeatGeneration_returnsSameResult(long seed, double x, double y, double z) {
        var result = PerlinNoise.generatePerlinNoise3d(seed, x, y, z);
        var sameInputsResult = PerlinNoise.generatePerlinNoise3d(seed, x, y, z);

        assertThat(result).isEqualTo(sameInputsResult);
    }

    @ParameterizedTest
    @MethodSource("random2dPerlinInputs")
    void generatePerlinNoise_repeatGeneration_returnsSameResult(long seed, double x, double y) {
        var result = PerlinNoise.generatePerlinNoise(seed, x, y);
        var sameInputsResult = PerlinNoise.generatePerlinNoise(seed, x, y);

        assertThat(result).isEqualTo(sameInputsResult);
    }

    @Test
    void generatePerlinNoise_differentSeed_createDifferentResults() {
        var result = PerlinNoise.generatePerlinNoise(567, 1.0, 2.5);
        assertThat(result).isEqualTo(-0.5);

        var otherSeed = PerlinNoise.generatePerlinNoise(566, 1.0, 2.5);
        assertThat(otherSeed).isZero();
    }

    @Test
    void generatePerlinNoise3d_differentSeed_createDifferentResults() {
        var result = PerlinNoise.generatePerlinNoise3d(567, 1.0, 2.5, 8);
        assertThat(result).isEqualTo(-0.5);

        var otherSeed = PerlinNoise.generatePerlinNoise3d(566, 1.0, 2.5, 8);
        assertThat(otherSeed).isZero();
    }

    @Test
    void generatePerlinNoise_negativeX_createsSameValueAsPositiveX() {
        var result = PerlinNoise.generatePerlinNoise(10, 14, 10);
        var second = PerlinNoise.generatePerlinNoise(10, -14, 10);

        assertThat(result).isEqualTo(second);
    }

    @Test
    void generatePerlinNoise3d_negativeX_createsSameValueAsPositiveX() {
        var result = PerlinNoise.generatePerlinNoise3d(10, 14, 10, 4);
        var second = PerlinNoise.generatePerlinNoise3d(10, -14, 10, 4);

        assertThat(result).isEqualTo(second);
    }

    static Stream<Arguments> random2dPerlinInputs() {
        return Stream.of(
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble()),
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble()),
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble())
        );
    }

    static Stream<Arguments> random3dPerlinInputs() {
        return Stream.of(
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()),
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble()),
                Arguments.of(RANDOM.nextLong(), RANDOM.nextDouble(), RANDOM.nextDouble(), RANDOM.nextDouble())
        );
    }
}
