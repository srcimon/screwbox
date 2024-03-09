package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.graphics.CircleDrawOptions.filled;
import static io.github.srcimon.screwbox.core.graphics.CircleDrawOptions.outline;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CircleDrawOptionsTest {

    @Test
    void toString_returnsInformationOnTheDrawing() {
        var options = filled(Color.RED);

        assertThat(options).hasToString("CircleDrawOptions{style=FILLED, color=Color [r=255, g=0, b=0, opacity=1.0], strokeWidth=0}");
    }

    @Test
    void strokeWidth_filledCircle_throwsException() {
        var options = filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width is only used when drawing circle outline");
    }

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = outline(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive");
    }

    @Test
    void testEquals() {
        assertThat(CircleDrawOptions.outline(Color.RED).strokeWidth(2))
                .isNotEqualTo(CircleDrawOptions.filled(Color.BLUE))
                .isNotEqualTo(CircleDrawOptions.filled(Color.RED))
                .isNotEqualTo(CircleDrawOptions.fading(Color.RED))
                .isNotEqualTo(CircleDrawOptions.outline(Color.RED).strokeWidth(4))
                .isEqualTo(CircleDrawOptions.outline(Color.RED).strokeWidth(2));
    }
}
