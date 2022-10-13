package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AngleTest {

    @Test
    void ofDegrees_degreesOutOfRange_returnsNewInstance() {
        var angle = Angle.ofDegrees(770);

        assertThat(angle.degrees()).isEqualTo(50);
    }

    @Test
    void ofDegrees_degreesInRange_returnsNewInstance() {
        var angle = Angle.ofDegrees(359);

        assertThat(angle.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noangle_isTrue() {
        var angle = Angle.ofDegrees(0);

        assertThat(angle.isNone()).isTrue();
    }

    @Test
    void isNone_someangle_isFalse() {
        var angle = Angle.ofDegrees(5);

        assertThat(angle.isNone()).isFalse();
    }

    @Test
    void equals_sameangle_isTrue() {
        var angle = Angle.ofDegrees(720);
        var other = Angle.ofDegrees(360);

        assertThat(angle).isEqualTo(other);
    }

    @Test
    void equals_otherangle_isFalse() {
        var angle = Angle.ofDegrees(269);
        var other = Angle.ofDegrees(100);

        assertThat(angle).isNotEqualTo(other);
    }

    @Test
    void radians_returnsRadiansValue() {
        assertThat(Angle.none().radians()).isEqualTo(0.0);
        assertThat(Angle.ofDegrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
    }

    @ParameterizedTest
    @CsvSource({ "100,100,135", "0,100,180", "-100,0,270" })
    void ofMomentum_returnsNewInstance(double x, double y, double value) {
        Angle angle = Angle.ofMomentum(Vector.of(x, y));

        assertThat(angle.degrees()).isEqualTo(value);
    }

    @Test
    void ofMomentum_returnsNewInstance() {
        Angle angle = Angle.ofMomentum(10, 20);

        assertThat(angle.degrees()).isCloseTo(153.4, offset(0.2));
    }
}
