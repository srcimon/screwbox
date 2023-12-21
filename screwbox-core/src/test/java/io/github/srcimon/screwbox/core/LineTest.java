package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

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
}
