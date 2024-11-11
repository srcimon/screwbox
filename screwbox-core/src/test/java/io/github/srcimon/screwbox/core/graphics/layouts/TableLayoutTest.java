package io.github.srcimon.screwbox.core.graphics.layouts;

import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableLayoutTest {

    private static final ScreenBounds SCREEN = new ScreenBounds(20, 10, 640, 480);

    @Test
    void calculateBounds_oneViewportButWithFillOption_fillsWholeScreen() {
        TableLayout layout = new TableLayout(8, true);

        var result = layout.calculateBounds(0, 1, 0, SCREEN);

        assertThat(result).isEqualTo(SCREEN);
    }

    @Test
    void calculateBounds_oneViewportButWithoutFillOption_fillsHalfScreen() {
        TableLayout layout = new TableLayout(2, false);

        var result = layout.calculateBounds(0, 1, 0, SCREEN);

        assertThat(result).isEqualTo(new ScreenBounds(20, 10, 320, 480));
    }

    @Test
    void calculateBounds_firstColumn_isHalfWidthOfScreen() {
        TableLayout layout = new TableLayout(2, true);
        var result = layout.calculateBounds(0, 2, 0, SCREEN);

        assertThat(result).isEqualTo(new ScreenBounds(20, 10, 320, 480));
    }

    @Test
    void calculateBounds_fourViewportsUsingPadding_paddingBetweenViewports() {
        TableLayout layout = new TableLayout(2, false);
        var first = layout.calculateBounds(0, 4, 6, SCREEN);
        var second = layout.calculateBounds(1, 4, 6, SCREEN);
        var third = layout.calculateBounds(2, 4, 6, SCREEN);
        var fourth = layout.calculateBounds(3, 4, 6, SCREEN);

        assertThat(first).isEqualTo(new ScreenBounds(20, 10, 317, 237));
        assertThat(second).isEqualTo(new ScreenBounds(343, 10, 317, 237));
        assertThat(third).isEqualTo(new ScreenBounds(20, 253, 317, 237));
        assertThat(fourth).isEqualTo(new ScreenBounds(343, 253, 317, 237));
    }

}
