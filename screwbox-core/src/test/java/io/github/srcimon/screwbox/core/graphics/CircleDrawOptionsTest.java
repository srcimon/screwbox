package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CircleDrawOptionsTest {

    @Test
    void strokeWidth_filledCircle_throwsException() {
        var options = CircleDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width is only used when drawing circle outline");
    }

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = CircleDrawOptions.outline(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive");
    }

}