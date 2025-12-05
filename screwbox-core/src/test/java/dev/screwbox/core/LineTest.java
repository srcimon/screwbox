package dev.screwbox.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static dev.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class LineTest {

    @Test
    void intersections_noOthers_isEmpty() {
        var intersections = Line.between($(10, 10), $(40, 40)).intersections(emptyList());

        assertThat(intersections).isEmpty();
    }

    @Test
    void intersections_someLinesIntersect_returnsPositions() {
        var others = List.of(
                Line.between($(0, 10), $(0, -0)),
                Line.between($(0, 10), $(0, 8)),
                Line.between($(40, 10), $(40, -8)));

        var intersections = Line.between($(0, 0), $(500, 0)).intersections(others);

        assertThat(intersections).containsExactly($(0, 0), $(40, 0));
    }

    @Test
    void intersectionPoint_intersect_returnIntersectionPoint() {
        Vector bird = $(20, 20);
        Vector mole = $(20, -20);
        Line birdMole = Line.between(bird, mole);
        Line horizon = Line.between(Vector.zero(), $(100, 0));

        Vector result = horizon.intersectionPoint(birdMole);

        assertThat(result).isEqualTo($(20, 0));
    }

    @Test
    void intersectionPoint_doesntIntersect_returnsNull() {
        Vector bird = $(20, 20);
        Vector mole = $(20, -20);
        Line birdMole = Line.between(bird, mole);
        Line pole = Line.between(Vector.zero(), $(0, 100));

        Vector result = pole.intersectionPoint(birdMole);

        assertThat(result).isNull();
    }

    @Test
    void intersects_intersects_returnsTrue() {
        Vector bird = $(20, 20);
        Vector mole = $(20, -20);
        Line birdMole = Line.between(bird, mole);
        Line horizon = Line.between(Vector.zero(), $(100, 0));

        var result = horizon.intersects(birdMole);

        assertThat(result).isTrue();
    }

    @Test
    void intersects_doesntIntersect_returnsFalse() {
        Vector bird = $(20, 20);
        Vector mole = $(20, -20);
        Line birdMole = Line.between(bird, mole);
        Line pole = Line.between(Vector.zero(), $(0, 100));

        var result = pole.intersects(birdMole);

        assertThat(result).isFalse();
    }

    @Test
    void normal_returnsNormalOfLine() {
        var result = Line.normal($(20, 10), 40);

        assertThat(result).isEqualTo(Line.between($(20, 10), $(20, 50)));
    }

    @Test
    void middle_validLine_calculatesMiddle() {
        var line = Line.between($(30, -5), $(10, 20));

        assertThat(line.middle()).isEqualTo($(20, 7.5));
    }

    @Test
    void closestPointOnLineToPoint_lineIsPoint_returnsStart() {
        var line = Line.between($(30, -5), $(30, -5));
        Vector closestPoint = line.closestPointOnLineToPoint($(499114, -4194));

        assertThat(closestPoint).isEqualTo($(30, -5));
    }

    @Test
    void closestPointOnLineToPoint_isNearStart_returnsStart() {
        var line = Line.between($(30, -5), $(430, 45));
        Vector closestPoint = line.closestPointOnLineToPoint($(2, 1));

        assertThat(closestPoint).isEqualTo($(30, -5));
    }

    @Test
    void closestPointOnLineToPoint_isNearEnd_returnsEnd() {
        var line = Line.between($(30, -5), $(430, 45));
        Vector closestPoint = line.closestPointOnLineToPoint($(252234, 1234));

        assertThat(closestPoint).isEqualTo($(430, 45));
    }

    @ParameterizedTest
    @CsvSource({
            "35, 6, 34.61, 10.40",
            "80, -26, 76.49, 14.07"
    })
    void closestPointOnLineToPoint_isNearSomePointOnLine_returnsPoint(double x, double y, double resultX, double resultY) {
        var line = Line.between($(30, 10), $(430, 45));
        Vector closestPoint = line.closestPointOnLineToPoint($(x, y));

        assertThat(closestPoint.x()).isEqualTo(resultX, offset(0.1));
        assertThat(closestPoint.y()).isEqualTo(resultY, offset(0.1));
    }
}
