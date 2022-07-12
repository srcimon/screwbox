package de.suzufa.screwbox.core.physics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;

class BordersTest {

    private static final Bounds BOX = Bounds.atPosition(10, 10, 42, 2);

    @Test
    void extractSegmentsMethod_ofAll_extractsAllSegments() {
        var method = Borders.ALL.extractSegmentsMethod();

        assertThat(method.apply(BOX)).contains(
                Segment.between(BOX.topLeft(), BOX.topRight()),
                Segment.between(BOX.topRight(), BOX.bottomRight()),
                Segment.between(BOX.bottomLeft(), BOX.topLeft()),
                Segment.between(BOX.topRight(), BOX.bottomRight()))
                .hasSize(4);
    }

    @Test
    void extractSegmentsMethod_ofTopOnly_extractsTopSegment() {
        var method = Borders.TOP_ONLY.extractSegmentsMethod();

        assertThat(method.apply(BOX))
                .contains(Segment.between(BOX.topLeft(), BOX.topRight()))
                .hasSize(1);
    }

    @Test
    void extractSegmentsMethod_ofVerticalOnly_extractsVerticalSegments() {
        var method = Borders.VERTICAL_ONLY.extractSegmentsMethod();

        assertThat(method.apply(BOX)).contains(
                Segment.between(BOX.bottomLeft(), BOX.topLeft()),
                Segment.between(BOX.topRight(), BOX.bottomRight()))
                .hasSize(2);
    }
}
