package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpriteFillOptionsTest {

    @Test
    void newInstance_scaleZero_throwsException() {
        assertThatThrownBy(() -> SpriteFillOptions.scale(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("scale must be positive");
    }

    @Test
    void newInstance_opacityNull_throwsException() {
        assertThatThrownBy(() -> SpriteFillOptions.scale(1).opacity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("opacity must not be null");
    }

    @Test
    void newInstance_offsetNull_throwsException() {
        assertThatThrownBy(() -> SpriteFillOptions.scale(1).offset(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("offset must not be null");
    }
}
