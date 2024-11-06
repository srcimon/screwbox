package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

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
}
