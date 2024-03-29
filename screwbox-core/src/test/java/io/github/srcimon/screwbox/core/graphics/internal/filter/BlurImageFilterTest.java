package io.github.srcimon.screwbox.core.graphics.internal.filter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlurImageFilterTest {

    @Test
    void newInstance_radiusTooSmall_throwsException() {
        assertThatThrownBy(() -> new BlurImageFilter(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("radius must be in range 1 to 6");
    }

    @Test
    void newInstance_radiusTooBig_throwsException() {
        assertThatThrownBy(() -> new BlurImageFilter(12))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("radius must be in range 1 to 6");
    }
}
