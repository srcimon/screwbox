package io.github.srcimon.screwbox.core;

import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Set;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class RotationTest {

    @Test
    void random_calledFourTimes_createsAtLeastTwoDistinctRotations() {
        Set<Rotation> randomRotations = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            randomRotations.add(Rotation.random());
            TestUtil.sleep(Duration.ofMillis(100));
        }
        assertThat(randomRotations).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void of_lineIsNull_throwsException() {
        assertThatThrownBy(() -> Rotation.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("line must not be null");
    }

    @Test
    void of_someLine_returnsRotation() {
        Line line = Line.between($(20, 10.5), $(12.1, -19));

        Rotation result = Rotation.of(line);

        assertThat(result.degrees()).isEqualTo(345, offset(0.1));
    }

    @Test
    void ofDegrees_degreesOutOfRange_returnsNewInstance() {
        var rotation = Rotation.degrees(770);

        assertThat(rotation.degrees()).isEqualTo(50);
    }

    @Test
    void ofDegrees_degreesInRange_returnsNewInstance() {
        var rotation = Rotation.degrees(359);

        assertThat(rotation.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noRotation_isTrue() {
        var rotation = Rotation.degrees(0);

        assertThat(rotation.isNone()).isTrue();
    }

    @Test
    void isNone_someRotation_isFalse() {
        var rotation = Rotation.degrees(5);

        assertThat(rotation.isNone()).isFalse();
    }

    @Test
    void equals_sameRotation_isTrue() {
        var rotation = Rotation.degrees(720);
        var other = Rotation.degrees(360);

        assertThat(rotation).isEqualTo(other);
    }

    @Test
    void equals_otherRotation_isFalse() {
        var rotation = Rotation.degrees(269);
        var other = Rotation.degrees(100);

        assertThat(rotation).isNotEqualTo(other);
    }

    @Test
    void radians_returnsRadiansValue() {
        assertThat(Rotation.none().radians()).isEqualTo(0.0);
        assertThat(Rotation.degrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
    }

    @ParameterizedTest
    @CsvSource({"100,100,135", "0,100,180", "-100,0,270"})
    void ofMovement_returnsNewInstance(double x, double y, double value) {
        Rotation rotation = Rotation.ofMovement(Vector.of(x, y));

        assertThat(rotation.degrees()).isEqualTo(value);
    }

    @Test
    void ofMovement_returnsNewInstance() {
        Rotation rotation = Rotation.ofMovement(10, 20);

        assertThat(rotation.degrees()).isCloseTo(153.4, offset(0.2));
    }

    @Test
    void applyOn_lineNull_throwsExceptions() {
        var rotation = Rotation.degrees(90);

        assertThatThrownBy(() -> rotation.applyOn(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("line must not be null");
    }

    @ParameterizedTest
    @CsvSource({"10.0,0.0,0.0,10.0,90",
            "12.4,33.9,-33.9,12.4,90",
            "12.4,33.9,23.2,27.6,-20"})
    void applyOn_validInput_returnsNewSegment(double x, double y, double toX, double toY, double degrees) {
        Line input = Line.between(Vector.zero(), $(x, y));

        Line rotated = Rotation.degrees(degrees).applyOn(input);

        assertThat(rotated.from()).isEqualTo(Vector.zero());
        assertThat(rotated.to().x()).isEqualTo(toX, offset(0.1));
        assertThat(rotated.to().y()).isEqualTo(toY, offset(0.1));
    }

    @Test
    void add_otherHasDegrees_returnsSum() {
        var result = Rotation.degrees(40).add(Rotation.degrees(340));
        assertThat(result.degrees()).isEqualTo(20);
    }
}
