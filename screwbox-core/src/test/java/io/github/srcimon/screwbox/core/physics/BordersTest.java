package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BordersTest {

    private static final Bounds BOX = Bounds.atPosition(10, 10, 42, 2);

    @Test
    void extractFrom_ofAll_extractsAllBorders() {
        var lines = Borders.ALL.extractFrom(BOX);

        assertThat(lines).containsExactly(
                Line.between(BOX.origin(), BOX.topRight()),
                Line.between(BOX.topRight(), BOX.bottomRight()),
                Line.between(BOX.bottomRight(), BOX.bottomLeft()),
                Line.between(BOX.bottomLeft(), BOX.origin()));
    }

    @Test
    void extractFrom_ofTopOnly_extractsTop() {
        var lines = Borders.TOP_ONLY.extractFrom(BOX);

        assertThat(lines).containsExactly(Line.between(BOX.origin(), BOX.topRight()));
    }

    @Test
    void extractFrom_ofVerticalOnly_extractsVerticals() {
        var lines = Borders.VERTICAL_ONLY.extractFrom(BOX);

        assertThat(lines).containsExactly(
                Line.between(BOX.topRight(), BOX.bottomRight()),
                Line.between(BOX.bottomLeft(), BOX.origin()));
    }
}
