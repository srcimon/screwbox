package dev.screwbox.core;

import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Set;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class AngleTest {

    @Test
    void random_calledFourTimes_createsAtLeastTwoDistinctRotations() {
        Set<Angle> randomRotations = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            randomRotations.add(Angle.random());
            TestUtil.sleep(Duration.ofMillis(100));
        }
        assertThat(randomRotations).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void of_lineIsNull_throwsException() {
        assertThatThrownBy(() -> Angle.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("line must not be null");
    }

    @Test
    void of_someLine_returnsRotation() {
        Line line = Line.between($(20, 10.5), $(12.1, -19));

        Angle result = Angle.of(line);

        assertThat(result.degrees()).isEqualTo(345, offset(0.1));
    }

    @Test
    void ofDegrees_degreesOutOfRange_returnsNewInstance() {
        var rotation = Angle.degrees(770);

        assertThat(rotation.degrees()).isEqualTo(50);
    }

    @Test
    void ofDegrees_degreesInRange_returnsNewInstance() {
        var rotation = Angle.degrees(359);

        assertThat(rotation.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noRotation_isTrue() {
        var rotation = Angle.degrees(0);

        assertThat(rotation.isNone()).isTrue();
    }

    @Test
    void isNone_someRotation_isFalse() {
        var rotation = Angle.degrees(5);

        assertThat(rotation.isNone()).isFalse();
    }

    @Test
    void equals_sameRotation_isTrue() {
        var rotation = Angle.degrees(720);
        var other = Angle.degrees(360);

        assertThat(rotation).isEqualTo(other);
    }

    @Test
    void equals_otherRotation_isFalse() {
        var rotation = Angle.degrees(269);
        var other = Angle.degrees(100);

        assertThat(rotation).isNotEqualTo(other);
    }

    @Test
    void radians_returnsRadiansValue() {
        assertThat(Angle.none().radians()).isEqualTo(0.0);
        assertThat(Angle.degrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
    }

    @ParameterizedTest
    @CsvSource({"100,100,135", "0,100,180", "-100,0,270"})
    void ofVector_returnsNewInstance(double x, double y, double value) {
        Angle rotation = Angle.ofVector(Vector.of(x, y));

        assertThat(rotation.degrees()).isEqualTo(value);
    }

    @Test
    void ofVector_returnsNewInstance() {
        Angle rotation = Angle.ofVector(10, 20);

        assertThat(rotation.degrees()).isCloseTo(153.4, offset(0.2));
    }

    @Test
    void applyOn_lineNull_throwsExceptions() {
        var rotation = Angle.degrees(90);

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

        Line rotated = Angle.degrees(degrees).applyOn(input);

        assertThat(rotated.from()).isEqualTo(Vector.zero());
        assertThat(rotated.to().x()).isEqualTo(toX, offset(0.1));
        assertThat(rotated.to().y()).isEqualTo(toY, offset(0.1));
    }

    @Test
    void add_otherHasDegrees_returnsSum() {
        var result = Angle.degrees(40).add(Angle.degrees(340));
        assertThat(result.degrees()).isEqualTo(20);
    }

    @Test
    void invert_returnsInvertedRotation() {
        var result = Angle.degrees(20).invert();

        assertThat(result.degrees()).isEqualTo(340);
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,0",
            "1,1,0",
            "5,20,15",
            "-5,-10,-5",
            "355,5,10",
            "5,355,-10"})
    void delta_always_returnsShortestDistance(double me, double other, double delta) {
        Angle rotation = Angle.degrees(me);
        Angle otherRotation = Angle.degrees(other);
        Angle expectedRotation = Angle.degrees(delta);

        assertThat(rotation.delta(otherRotation)).isEqualTo(expectedRotation);
    }

    @Test
    void delta_otherIsNull_throwsException() {
        final var rotation = Angle.degrees(10);

        assertThatThrownBy(() -> rotation.delta(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("other must not be null");
    }

    @Test
    void addDegrees_positiveDegrees_addsDegrees() {
        final var rotation = Angle.degrees(10);

        assertThat(rotation.addDegrees(2)).isEqualTo(Angle.degrees(12));
    }

}
