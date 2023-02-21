package io.github.simonbas.screwbox.core.physics;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Segment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class BordersTest {

    private static final Bounds BOX = Bounds.atPosition(10, 10, 42, 2);

    @Test
    void extractSegmentsMethod_ofAll_extractsAllSegments() {
        var method = Borders.ALL.extractSegmentsMethod();

        Assertions.assertThat(method.apply(BOX)).contains(
                        Segment.between(BOX.origin(), BOX.topRight()),
                        Segment.between(BOX.topRight(), BOX.bottomRight()),
                        Segment.between(BOX.bottomLeft(), BOX.origin()),
                        Segment.between(BOX.topRight(), BOX.bottomRight()))
                .hasSize(4);
    }

    @Test
    void extractSegmentsMethod_ofTopOnly_extractsTopSegment() {
        var method = Borders.TOP_ONLY.extractSegmentsMethod();

        Assertions.assertThat(method.apply(BOX))
                .contains(Segment.between(BOX.origin(), BOX.topRight()))
                .hasSize(1);
    }

    @Test
    void extractSegmentsMethod_ofVerticalOnly_extractsVerticalSegments() {
        var method = Borders.VERTICAL_ONLY.extractSegmentsMethod();

        Assertions.assertThat(method.apply(BOX)).contains(
                        Segment.between(BOX.bottomLeft(), BOX.origin()),
                        Segment.between(BOX.topRight(), BOX.bottomRight()))
                .hasSize(2);
    }
}
