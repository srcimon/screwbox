package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.layouts.TableLayout;
import io.github.srcimon.screwbox.core.graphics.layouts.VerticalLayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SplitscreenOptionsTest {

    @Test
    void viewports_tooManyViewports_throwsException() {
        assertThatThrownBy(() -> SplitscreenOptions.viewports(65))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("split screen supports only up to 64 viewports (what is your monitor like?)");
    }

    @Test
    void viewport_negativeViewportCount_throwsException() {
        assertThatThrownBy(() -> SplitscreenOptions.viewports(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("split screen must have at least one viewport");
    }

    @Test
    void newInstance_tableLayoutWithFourViewportsAnNoBorders_setsAllProperties() {
        var options = SplitscreenOptions.viewports(4).tableLayout().noBorders();

        assertThat(options.viewportCount()).isEqualTo(4);
        assertThat(options.layout()).isInstanceOf(TableLayout.class);
        assertThat(options.borders()).isNull();
    }

    @Test
    void newInstance_verticalLayoutWithTwoViewportsAndBlueBorders_setsAllProperties() {
        var borders = LineDrawOptions.color(Color.BLUE).strokeWidth(2);

        var options = SplitscreenOptions.viewports(2).verticalLayout().borders(borders);

        assertThat(options.viewportCount()).isEqualTo(2);
        assertThat(options.layout()).isInstanceOf(VerticalLayout.class);
        assertThat(options.borders()).isEqualTo(borders);
    }
}
