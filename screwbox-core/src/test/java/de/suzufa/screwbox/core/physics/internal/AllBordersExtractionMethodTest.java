package de.suzufa.screwbox.core.physics.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

class AllBordersExtractionMethodTest {

    private AllBordersExtractionMethod method;

    @BeforeEach
    void beforeEach() {
        method = new AllBordersExtractionMethod();
    }

    @Test
    void segments_returnsFourSegments() {
        Bounds player = Bounds.atPosition(10, 10, 42, 2);

        Vector upperLeft = Vector.of(-11, 9);
        Vector upperRight = Vector.of(31, 9);
        Vector lowerLeft = Vector.of(-11, 11);
        Vector lowerRight = Vector.of(31, 11);

        assertThat(method.segments(player)).contains(
                Segment.between(upperLeft, upperRight),
                Segment.between(upperRight, lowerRight),
                Segment.between(lowerLeft, upperLeft),
                Segment.between(upperRight, lowerRight))
                .hasSize(4);
    }
}
