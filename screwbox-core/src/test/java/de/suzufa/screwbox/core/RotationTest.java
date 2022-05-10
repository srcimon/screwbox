package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RotationTest {

    @Test
    void ofDegrees_degreesOutOfRange_returnsNewInstance() {
        var rotation = Rotation.ofDegrees(770);

        assertThat(rotation.degrees()).isEqualTo(50);
    }

    @Test
    void ofDegrees_degreesInRange_returnsNewInstance() {
        var rotation = Rotation.ofDegrees(359);

        assertThat(rotation.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noRotation_isTrue() {
        var rotation = Rotation.ofDegrees(0);

        assertThat(rotation.isNone()).isTrue();
    }

    @Test
    void isNone_someRotation_isFalse() {
        var rotation = Rotation.ofDegrees(5);

        assertThat(rotation.isNone()).isFalse();
    }

    @Test
    void equals_sameRotation_isTrue() {
        var rotation = Rotation.ofDegrees(720);
        var other = Rotation.ofDegrees(360);

        assertThat(rotation).isEqualTo(other);
    }

    @Test
    void equals_otherRotation_isFalse() {
        var rotation = Rotation.ofDegrees(269);
        var other = Rotation.ofDegrees(100);

        assertThat(rotation).isNotEqualTo(other);
    }

    @Test
    void radians_returnsRadiansValue() {
        assertThat(Rotation.none().radians()).isEqualTo(0.0);
        assertThat(Rotation.ofDegrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
    }

    @ParameterizedTest
    @CsvSource({ "100,100,135", "0,100,180", "-100,0,270" })
    void ofMomentum_returnsNewInstance(double x, double y, double angle) {
        Rotation rotation = Rotation.ofMomentum(Vector.of(x, y));

        assertThat(rotation.degrees()).isEqualTo(angle);
    }

    @Test
    void ofMomentum_returnsNewInstance() {
        Rotation rotation = Rotation.ofMomentum(10, 20);

        assertThat(rotation.degrees()).isCloseTo(153.4, offset(0.2));
    }
}
