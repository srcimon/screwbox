package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.SPLINE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PolygonDrawOptionsTest {

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = PolygonDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive (actual value: 0)");
    }

    @Test
    void smoothing_null_throwsException() {
        var options = PolygonDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.smoothing(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("smoothing must not be null");
    }

    @Test
    void newInstance_allPropertiesSet_hasValues() {
        var options = PolygonDrawOptions.outline(Color.RED)
                .strokeWidth(4)
                .smoothing(SPLINE)
                .drawOrder(9);

        assertThat(options.smoothing()).isEqualTo(SPLINE);
        assertThat(options.drawOrder()).isEqualTo(9);
        assertThat(options.strokeWidth()).isEqualTo(4);
        assertThat(options.style()).isEqualTo(PolygonDrawOptions.Style.OUTLINE);
    }
}