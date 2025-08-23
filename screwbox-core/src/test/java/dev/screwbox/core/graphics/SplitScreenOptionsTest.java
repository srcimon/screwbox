package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.layouts.TableLayout;
import dev.screwbox.core.graphics.layouts.VerticalLayout;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SplitScreenOptionsTest {

    @Test
    void viewports_tooManyViewports_throwsException() {
        assertThatThrownBy(() -> SplitScreenOptions.viewports(65))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("split screen supports only up to 64 viewports (what is your monitor like?) (actual value: 65)");
    }

    @Test
    void viewport_negativeViewportCount_throwsException() {
        assertThatThrownBy(() -> SplitScreenOptions.viewports(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("split screen must have at least one viewport (actual value: -1)");
    }

    @Test
    void newInstance_tableLayoutWithFourViewportsNoPadding_setsAllProperties() {
        var options = SplitScreenOptions.viewports(4).tableLayout().noPadding();

        assertThat(options.viewportCount()).isEqualTo(4);
        assertThat(options.layout()).isInstanceOf(TableLayout.class);
        assertThat(options.padding()).isZero();
    }

    @Test
    void newInstance_verticalLayoutWithTwoViewportsAndPadding_setsAllProperties() {
        var options = SplitScreenOptions.viewports(2).verticalLayout().padding(12);

        assertThat(options.viewportCount()).isEqualTo(2);
        assertThat(options.layout()).isInstanceOf(VerticalLayout.class);
        assertThat(options.padding()).isEqualTo(12);
    }

    @Test
    void padding_tooLarge_throwsException() {
        var options = SplitScreenOptions.viewports(2);

        assertThatThrownBy(() -> options.padding(33))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("padding has max value of 32 (actual value: 33)");
    }

    @Test
    void padding_negative_throwsException() {
        var options = SplitScreenOptions.viewports(2);

        assertThatThrownBy(() -> options.padding(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("padding must be positive (actual value: -2)");
    }
}
