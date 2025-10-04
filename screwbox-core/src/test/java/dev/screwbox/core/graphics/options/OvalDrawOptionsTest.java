package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OvalDrawOptionsTest {

    @Test
    void strokeWidth_filledCircle_throwsException() {
        var options = OvalDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width is only used when drawing circle outline");
    }

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = OvalDrawOptions.outline(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive (actual value: 0)");
    }

    @Test
    void newInstance_fadingOptions_setsAllProperties() {
        var options = OvalDrawOptions.fading(Color.RED)
                .startAngle(Angle.degrees(40))
                .arcAngle(Angle.degrees(90));

        assertThat(options.style()).isEqualTo(OvalDrawOptions.Style.FADING);
        assertThat(options.strokeWidth()).isOne();
        assertThat(options.color()).isEqualTo(Color.RED);
        assertThat(options.startAngle()).isEqualTo(Angle.degrees(40));
        assertThat(options.arcAngle()).isEqualTo(Angle.degrees(90));
    }
}