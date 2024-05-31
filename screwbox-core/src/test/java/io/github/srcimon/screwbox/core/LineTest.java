package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.github.srcimon.screwbox.core.Vector.$;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

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
    void intersectionPoint_dosntIntersect_returnsNull() {
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
}
