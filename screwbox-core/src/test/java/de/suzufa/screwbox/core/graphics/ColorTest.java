package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.Percentage;

class ColorTest {

    @Test
    void newInstance_negativeColorValue_throwsException() {
        assertThatThrownBy(() -> Color.rgb(-10, 200, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid color value (0-255): -10");
    }

    @Test
    void newInstance_tooHighInvalidColor_throwsException() {
        assertThatThrownBy(() -> Color.rgb(0, 200, 260))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid color value (0-255): 260");
    }

    @Test
    void newInstance_validRgbValues_returnsColor() {
        Color color = Color.rgb(10, 20, 30);

        assertThat(color.r()).isEqualTo(10);
        assertThat(color.g()).isEqualTo(20);
        assertThat(color.b()).isEqualTo(30);
    }

    @Test
    void withOpacity_returnsNewInstanceWithDifferentOpacity() {
        Color color = Color.rgb(10, 20, 30).withOpacity(0.5);

        assertThat(color.r()).isEqualTo(10);
        assertThat(color.g()).isEqualTo(20);
        assertThat(color.b()).isEqualTo(30);
        assertThat(color.opacity()).isEqualTo(Percentage.half());
    }

    @Test
    void toString_returnsString() {
        var color = Color.rgb(10, 20, 30).withOpacity(0.5);

        assertThat(color).hasToString("Color [r=10, g=20, b=30, opacity=0.5]");
    }
}
