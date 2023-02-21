package io.github.simonbas.screwbox.core.graphics;

import io.github.simonbas.screwbox.core.Percent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void opacity_returnsNewInstanceWithDifferentOpacity() {
        Color color = Color.rgb(10, 20, 30).opacity(0.5);

        assertThat(color.r()).isEqualTo(10);
        assertThat(color.g()).isEqualTo(20);
        assertThat(color.b()).isEqualTo(30);
        assertThat(color.opacity()).isEqualTo(Percent.half());
    }

    @Test
    void toString_returnsString() {
        var color = Color.rgb(10, 20, 30).opacity(0.5);

        assertThat(color).hasToString("Color [r=10, g=20, b=30, opacity=0.5]");
    }

    @Test
    void random_calledTenTimes_hasAtLeastFiveDifferentColors() {
        Set<Color> colors = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            colors.add(Color.random());
        }

        Assertions.assertThat(colors).hasSizeBetween(5, 10);
    }
}
