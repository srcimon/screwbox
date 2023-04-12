package io.github.srcimon.screwbox.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SegmentTest {

    @Test
    void intersectionPoint_intersect_returnIntersectionPoint() {
        Vector bird = Vector.of(20, 20);
        Vector mole = Vector.of(20, -20);
        Segment birdMole = Segment.between(bird, mole);
        Segment horizon = Segment.between(Vector.zero(), Vector.of(100, 0));

        Vector result = horizon.intersectionPoint(birdMole);

        assertThat(result).isEqualTo(Vector.of(20, 0));
    }

    @Test
    void intersectionPoint_dosntIntersect_returnsNull() {
        Vector bird = Vector.of(20, 20);
        Vector mole = Vector.of(20, -20);
        Segment birdMole = Segment.between(bird, mole);
        Segment pole = Segment.between(Vector.zero(), Vector.of(0, 100));

        Vector result = pole.intersectionPoint(birdMole);

        assertThat(result).isNull();
    }

    @Test
    void intersects_intersects_returnsTrue() {
        Vector bird = Vector.of(20, 20);
        Vector mole = Vector.of(20, -20);
        Segment birdMole = Segment.between(bird, mole);
        Segment horizon = Segment.between(Vector.zero(), Vector.of(100, 0));

        var result = horizon.intersects(birdMole);

        assertThat(result).isTrue();
    }

    @Test
    void intersects_doesntIntersect_returnsFalse() {
        Vector bird = Vector.of(20, 20);
        Vector mole = Vector.of(20, -20);
        Segment birdMole = Segment.between(bird, mole);
        Segment pole = Segment.between(Vector.zero(), Vector.of(0, 100));

        var result = pole.intersects(birdMole);

        assertThat(result).isFalse();
    }
}
