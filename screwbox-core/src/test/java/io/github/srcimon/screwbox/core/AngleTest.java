package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class AngleTest {

    @Test
    void of_segmentIsNull_throwsException() {
        assertThatThrownBy(() -> Angle.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("segment must not be null");
    }

    @Test
    void of_someSegment_returnsAngle() {
        Segment segment = Segment.between(Vector.$(20, 10.5), Vector.$(12.1, -19));

        Angle result = Angle.of(segment);

        assertThat(result.degrees()).isEqualTo(345, offset(0.1));
    }

    @Test
    void ofDegrees_degreesOutOfRange_returnsNewInstance() {
        var angle = Angle.degrees(770);

        assertThat(angle.degrees()).isEqualTo(50);
    }

    @Test
    void ofDegrees_degreesInRange_returnsNewInstance() {
        var angle = Angle.degrees(359);

        assertThat(angle.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noangle_isTrue() {
        var angle = Angle.degrees(0);

        assertThat(angle.isNone()).isTrue();
    }

    @Test
    void isNone_someangle_isFalse() {
        var angle = Angle.degrees(5);

        assertThat(angle.isNone()).isFalse();
    }

    @Test
    void equals_sameangle_isTrue() {
        var angle = Angle.degrees(720);
        var other = Angle.degrees(360);

        assertThat(angle).isEqualTo(other);
    }

    @Test
    void equals_otherangle_isFalse() {
        var angle = Angle.degrees(269);
        var other = Angle.degrees(100);

        assertThat(angle).isNotEqualTo(other);
    }

    @Test
    void radians_returnsRadiansValue() {
        assertThat(Angle.none().radians()).isEqualTo(0.0);
        assertThat(Angle.degrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
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

    @Test
    void rotate_segmentNull_throwsExceptions() {
        var angle = Angle.degrees(90);

        assertThatThrownBy(() -> angle.rotate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("segment must not be null");
    }

    @ParameterizedTest
    @CsvSource({ "10.0,0.0,0.0,10.0,90",
            "12.4,33.9,-33.9,12.4,90",
            "12.4,33.9,23.2,27.6,-20"

    })
    void rotate_validInput_returnsNewSegment(double x, double y, double toX, double toY, double degrees) {
        Segment input = Segment.between(Vector.zero(), Vector.$(x, y));

        Segment rotated = Angle.degrees(degrees).rotate(input);

        assertThat(rotated.from()).isEqualTo(Vector.zero());
        assertThat(rotated.to().x()).isEqualTo(toX, offset(0.1));
        assertThat(rotated.to().y()).isEqualTo(toY, offset(0.1));
    }
}
