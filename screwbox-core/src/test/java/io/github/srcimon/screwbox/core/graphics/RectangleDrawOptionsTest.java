package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.Rotation.degrees;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.filled;
import static io.github.srcimon.screwbox.core.graphics.RectangleDrawOptions.outline;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RectangleDrawOptionsTest {

    @Test
    void toString_returnsInformationOnTheDrawing() {
        var options = filled(Color.RED).rotation(degrees(4));

        assertThat(options).hasToString("RectangleOptions{isFilled=true, color=Color [r=255, g=0, b=0, opacity=1.0], strokeWidth=1, rotation=Rotation [4.0°]}");
    }

    @Test
    void strokeWidth_filledRectangle_throwsException() {
        var options = filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width is not used when drawing filled rectangles");
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
        assertThat(filled(Color.RED).rotation(degrees(5)))
                .isNotEqualTo(filled(Color.BLUE).rotation(degrees(5)))
                .isNotEqualTo(filled(Color.RED).rotation(degrees(4)))
                .isNotEqualTo(outline(Color.RED).rotation(degrees(5)))
                .isNotEqualTo(outline(Color.RED).rotation(degrees(5)).strokeWidth(2))
                .isEqualTo(filled(Color.RED).rotation(degrees(5)));
    }
}
