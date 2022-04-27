package de.suzufa.screwbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

import org.junit.jupiter.api.Test;

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
}
