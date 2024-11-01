package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableLayoutTest {

    private static final ScreenBounds SCREEN = new ScreenBounds(20, 10, 640, 480);

    @Test
    void calculateBounds_oneViewportButWithFillOption_fillsWholeScreen() {
        TableLayout layout = new TableLayout(8, true);

        var result = layout.calculateBounds(0, 1, SCREEN);

        assertThat(result).isEqualTo(SCREEN);
    }

    @Test
    void calculateBounds_oneViewportButWithoutFillOption_fillsHalfScreen() {
        TableLayout layout = new TableLayout(2, false);

        var result = layout.calculateBounds(0, 1, SCREEN);

        assertThat(result).isEqualTo(new ScreenBounds(20, 10, 320, 480));
    }

    @Test
    void calculateBounds_firstColumn_isHalfWidthOfScreen() {
        TableLayout layout = new TableLayout(2, true);
        var result = layout.calculateBounds(0, 2, SCREEN);

        assertThat(result).isEqualTo(new ScreenBounds(20, 10, 320, 480));
    }

}
