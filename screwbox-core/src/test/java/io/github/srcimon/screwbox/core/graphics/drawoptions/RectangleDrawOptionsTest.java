package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RectangleDrawOptionsTest {

    @Test
    void strokeWidth_filledRectangle_throwsException() {
        var options = RectangleDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width is only used when drawing outline of rectangles");
    }

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = RectangleDrawOptions.outline(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive");
    }

    @Test
    void filled_createsFilledOptions() {
        var options = RectangleDrawOptions.filled(Color.YELLOW).rotation(Rotation.degrees(45));

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.FILLED);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Rotation.degrees(45));
        assertThat(options.strokeWidth()).isEqualTo(1);
    }

    @Test
    void outline_createsOutlineOptions() {
        var options = RectangleDrawOptions.outline(Color.YELLOW).strokeWidth(5);

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.OUTLINE);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Rotation.none());
        assertThat(options.strokeWidth()).isEqualTo(5);
    }
}
