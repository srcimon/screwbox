package dev.screwbox.core;

import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Set;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.Vector.x;
import static dev.screwbox.core.Vector.y;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

class AngleTest {

    @ParameterizedTest
    @CsvSource({
        "0,0",
        "1,0",
        "0.5,180",
        "0.25,90",
        "0.1,36"})
    void circle_specifiedPercentage_returnsCorrespondingAngle(double percent, double degrees) {
        var angle = Angle.circle(Percent.of(percent));

        assertThat(angle.degrees()).isEqualTo(degrees);
    }

    @Test
    void random_calledFourTimes_createsAtLeastTwoDistinctAngles() {
        Set<Angle> randomAngles = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            randomAngles.add(Angle.random());
            TestUtil.sleep(Duration.ofMillis(100));
        }
        assertThat(randomAngles).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void of_lineIsNull_throwsException() {
        assertThatThrownBy(() -> Angle.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("line must not be null");
    }

    @Test
    void of_someLine_returnsAngle() {
        Line line = Line.between($(20, 10.5), $(12.1, -19));

        Angle result = Angle.of(line);

        assertThat(result.degrees()).isEqualTo(345, offset(0.1));
    }

    @Test
    void ofLineBetweenPoints_toValidPoints_returnsAngle() {
        Angle result = Angle.ofLineBetweenPoints($(20, 10.5), $(12.1, -19));

        assertThat(result.degrees()).isEqualTo(345, offset(0.1));
    }

    @Test
    void degrees_degreesOutOfRange_returnsNewInstance() {
        var angle = Angle.degrees(770);

        assertThat(angle.degrees()).isEqualTo(50);
    }

    @Test
    void degrees_degreesNegativeOutOfRange_returnsNewInstance() {
        var angle = Angle.degrees(-770);

        assertThat(angle.degrees()).isEqualTo(-50);
    }

    @Test
    void degrees_degreesInRange_returnsNewInstance() {
        var angle = Angle.degrees(359);

        assertThat(angle.degrees()).isEqualTo(359);
    }

    @Test
    void isNone_noRotation_isTrue() {
        var angle = Angle.degrees(0);

        assertThat(angle.isZero()).isTrue();
    }

    @Test
    void isNone_someRotation_isFalse() {
        var angle = Angle.degrees(5);

        assertThat(angle.isZero()).isFalse();
    }

    @Test
    void equals_sameAngle_isTrue() {
        var angle = Angle.degrees(720);
        var other = Angle.degrees(360);

        assertThat(angle).isEqualTo(other);
    }

    @Test
    void equals_otherAngle_isFalse() {
        var angle = Angle.degrees(269);
        var other = Angle.degrees(100);

        assertThat(angle).isNotEqualTo(other);
    }

    @Test
    void radians_degreesSpecified_returnsRadiansValue() {
        assertThat(Angle.none().radians()).isEqualTo(0.0);
        assertThat(Angle.degrees(180).radians()).isCloseTo(3.14159, withPercentage(1));
    }

    @Test
    void radians_radiansSpecified_returnsRadiansValue() {
        assertThat(Angle.radians(4).radians()).isEqualTo(4);
    }

    @ParameterizedTest
    @CsvSource({"100,100,135", "0,100,180", "-100,0,270"})
    void ofVector_returnsNewInstance(double x, double y, double value) {
        Angle angle = Angle.ofVector(Vector.of(x, y));

        assertThat(angle.degrees()).isEqualTo(value);
    }

    @Test
    void ofVector_returnsNewInstance() {
        Angle angle = Angle.ofVector(10, 20);

        assertThat(angle.degrees()).isCloseTo(153.4, offset(0.2));
    }

    @Test
    void rotate_lineNull_throwsExceptions() {
        var angle = Angle.degrees(90);

        assertThatThrownBy(() -> angle.rotate(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("line must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "10.0, 0.0, 0.0, 10.0, 90",
        "12.4, 33.9, -33.9, 12.4, 90",
        "12.4, 33.9, 23.2, 27.6, -20"
    })
    void rotate_validInput_returnsNewLine(double x, double y, double toX, double toY, double degrees) {
        Line input = Line.between(Vector.zero(), $(x, y));

        Line rotated = Angle.degrees(degrees).rotate(input);

        assertThat(rotated.start()).isEqualTo(Vector.zero());
        assertThat(rotated.end().x()).isEqualTo(toX, offset(0.1));
        assertThat(rotated.end().y()).isEqualTo(toY, offset(0.1));
    }

    @ParameterizedTest
    @CsvSource({
        "10.0, 0.0, -2.0, -8.0, 90",
        "12.4, 33.9, -35.9, -5.6, 90",
        "12.4, 33.9, 27.15, 29.75, -20"
    })
    void rotatePointAroundCenter_validInput_returnsNew(double x, double y, double toX, double toY, double degrees) {
        var rotated = Angle.degrees(degrees).rotateAroundCenter($(8, -10), $(x, y));

        assertThat(rotated.x()).isEqualTo(toX, offset(0.1));
        assertThat(rotated.y()).isEqualTo(toY, offset(0.1));
    }

    @Test
    void add_otherHasDegrees_returnsSum() {
        var result = Angle.degrees(40).add(Angle.degrees(340));
        assertThat(result.degrees()).isEqualTo(20);
    }

    @Test
    void invert_returnsInvertedAngle() {
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
        Angle angle = Angle.degrees(me);
        Angle otherAngle = Angle.degrees(other);
        Angle expectedAngle = Angle.degrees(delta);

        assertThat(angle.delta(otherAngle)).isEqualTo(expectedAngle);
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

    @Test
    void betweenLines_moreThan180Degrees_returnsAngle() {
        var angle = Angle.betweenLines(Vector.zero(), x(10), y(10));
        assertThat(angle).isEqualTo(Angle.degrees(270));
    }

    @Test
    void betweenLines_lessThan180Degrees_returnsAngle() {
        var angle = Angle.betweenLines(Vector.zero(), x(10), $(5, -5));
        assertThat(angle).isEqualTo(Angle.degrees(45));
    }

    @Test
    void rotateAroundCenter_lineIsNull_throwsException() {
        Angle angle = Angle.degrees(45);
        assertThatThrownBy(() -> angle.rotateAroundCenter(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("line must not be null");
    }

    @Test
    void rotateAroundCenter_horizontalLine_returnsRotatedLine() {
        var line = Line.between($(30, 20), $(60, 20));
        var rotated = Angle.degrees(45).rotateAroundCenter(line);
        assertThat(rotated.center()).isEqualTo($(45, 20));
    }
}
