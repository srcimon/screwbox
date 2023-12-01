package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BordersTest {

    private static final Bounds BOX = Bounds.atPosition(10, 10, 42, 2);

    @Test
    void extractBorders_ofAll_extractsAllBorders() {
        var Lines = Borders.ALL.extractBorders(BOX);

        assertThat(Lines).containsExactly(
                Line.between(BOX.origin(), BOX.topRight()),
                Line.between(BOX.topRight(), BOX.bottomRight()),
                Line.between(BOX.bottomRight(), BOX.bottomLeft()),
                Line.between(BOX.bottomLeft(), BOX.origin()));
    }

    @Test
    void extractBorders_ofTopOnly_extractsTop() {
        var lines = Borders.TOP_ONLY.extractBorders(BOX);

        assertThat(lines).containsExactly(Line.between(BOX.origin(), BOX.topRight()));
    }

    @Test
    void extractBorders_ofVerticalOnly_extractsVerticals() {
        var lines = Borders.VERTICAL_ONLY.extractBorders(BOX);

        assertThat(lines).containsExactly(
                Line.between(BOX.topRight(), BOX.bottomRight()),
                Line.between(BOX.bottomLeft(), BOX.origin()));
    }
}
