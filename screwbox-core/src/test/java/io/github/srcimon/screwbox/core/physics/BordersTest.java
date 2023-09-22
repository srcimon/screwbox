package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Segment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BordersTest {

    private static final Bounds BOX = Bounds.atPosition(10, 10, 42, 2);

    @Test
    void extractSegments_ofAll_extractsAllSegments() {
        var segments = Borders.ALL.extractSegments(BOX);

        assertThat(segments).containsExactly(
                Segment.between(BOX.origin(), BOX.topRight()),
                Segment.between(BOX.topRight(), BOX.bottomRight()),
                Segment.between(BOX.bottomRight(), BOX.bottomLeft()),
                Segment.between(BOX.bottomLeft(), BOX.origin()));
    }

    @Test
    void extractSegments_ofTopOnly_extractsTopSegment() {
        var segments = Borders.TOP_ONLY.extractSegments(BOX);

        assertThat(segments).containsExactly(Segment.between(BOX.origin(), BOX.topRight()));
    }

    @Test
    void extractSegments_ofVerticalOnly_extractsVerticalSegments() {
        var segments = Borders.VERTICAL_ONLY.extractSegments(BOX);

        assertThat(segments).containsExactly(
                Segment.between(BOX.topRight(), BOX.bottomRight()),
                Segment.between(BOX.bottomLeft(), BOX.origin()));
    }
}
