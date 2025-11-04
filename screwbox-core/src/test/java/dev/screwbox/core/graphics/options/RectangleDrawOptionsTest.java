package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Color;
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
                .hasMessage("stroke width must be positive (actual value: 0)");
    }

    @Test
    void curveRadius_negativeRadius_throwsException() {
        var options = RectangleDrawOptions.outline(Color.RED);

        assertThatThrownBy(() -> options.curveRadius(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("curve radius must be positive (actual value: -1)");
    }

    @Test
    void filled_createsFilledOptions() {
        var options = RectangleDrawOptions.filled(Color.YELLOW).rotation(Angle.degrees(45));

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.FILLED);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Angle.degrees(45));
        assertThat(options.strokeWidth()).isEqualTo(1);
        assertThat(options.isCurved()).isFalse();
        assertThat(options.curveRadius()).isZero();
        assertThat(options.drawOrder()).isZero();
    }

    @Test
    void outline_createsOutlineOptions() {
        var options = RectangleDrawOptions.outline(Color.YELLOW)
                .strokeWidth(5)
                .curveRadius(9)
                .drawOrder(90);

        assertThat(options.style()).isEqualTo(RectangleDrawOptions.Style.OUTLINE);
        assertThat(options.color()).isEqualTo(Color.YELLOW);
        assertThat(options.rotation()).isEqualTo(Angle.none());
        assertThat(options.drawOrder()).isEqualTo(90);
        assertThat(options.strokeWidth()).isEqualTo(5);
        assertThat(options.isCurved()).isTrue();
        assertThat(options.curveRadius()).isEqualTo(9);
    }
}
