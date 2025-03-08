package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ColorTest {

    @ParameterizedTest
    @CsvSource({
            "#ffffff, 255, 255, 255, 1",
            "#ff0000, 255, 0, 0, 1",
            "#00ff00, 0, 255, 0, 1",
            "#0000ff, 0, 0, 255, 1",
            "#00ffffff, 255, 255, 255, 0",
    })
    void hex_validInput_returnsColor(String hex, int r, int g, int b, double opacity) {
        var color = Color.hex(hex);

        Color expectedColor = Color.rgb(r, g, b, Percent.of(opacity));
        assertThat(color).isEqualTo(expectedColor);
    }

    @ParameterizedTest
    @CsvSource({
            "#ffffff, 255, 255, 255, 1",
            "#ff0000, 255, 0, 0, 1",
            "#00ff00, 0, 255, 0, 1",
            "#0000ff, 0, 0, 255, 1",
            "#00ffffff, 255, 255, 255, 0",
    })
    void hex_validColor_returnsHex(String hex, int r, int g, int b, double opacity) {
        var color = Color.rgb(r, g, b, Percent.of(opacity));

        assertThat(color.hex()).isEqualTo(hex);
    }

    @Test
    void hex_nonDecimalInput_throwsException() {
        assertThatThrownBy(() -> Color.hex("#00GÖ00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("hex value contains non hexadecimal value: GÖ");
    }

    @Test
    void hex_hexValueNull_throwsException() {
        assertThatThrownBy(() -> Color.hex(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("hex value must not be NULL");
    }

    @Test
    void hex_doesntStartWithHash_throwsException() {
        assertThatThrownBy(() -> Color.hex("D2D2D2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("hex value must start with '#'");
    }

    @Test
    void hex_invalidFormat_throwsException() {
        assertThatThrownBy(() -> Color.hex("#D2D2D2D2D2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("unknown hex format: #D2D2D2D2D2");
    }

    @Test
    void newInstance_negativeColorValue_throwsException() {
        assertThatThrownBy(() -> Color.rgb(-10, 200, 100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid red color value (0-255)");
    }

    @Test
    void newInstance_tooHighInvalidColor_throwsException() {
        assertThatThrownBy(() -> Color.rgb(0, 200, 260))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid blue color value (0-255)");
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

        assertThat(colors).hasSizeBetween(5, 10);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1239132, 123199, 130004})
    void rgb_validInput_isSameAsInput(int rgb) {
        assertThat(Color.rgb(rgb).rgb()).isEqualTo(rgb);
    }

    @Test
    void greyscale_colorIn_greyscaleOut() {
        var greyscale = Color.rgb(10, 40, 90).greyscale();
        assertThat(greyscale).isEqualTo(Color.rgb(46, 46, 46));
    }

    @Test
    void greyscale_greyscaleIn_greyscaleOut() {
        var greyscale = Color.rgb(46, 46, 46).greyscale();
        assertThat(greyscale).isEqualTo(Color.rgb(46, 46, 46));
    }

    @Test
    void alpha_opacityMax_hasValue() {
        Color color = Color.rgb(100, 40, 40, Percent.max());

        var alpha = color.alpha();

        assertThat(alpha).isEqualTo(-16777216);
    }

    @Test
    void alpha_opacityNone_isZero() {
        Color color = Color.rgb(100, 40, 40, Percent.zero());

        var alpha = color.alpha();

        assertThat(alpha).isZero();
    }

    @Test
    void invert_colorWithOpacity_returnsInvertedColorWIthOpacity() {
        Color color = Color.rgb(100, 40, 40, Percent.threeQuarter());

        Color inverted = color.invert();

        assertThat(inverted).isEqualTo(Color.rgb(155, 215, 215, Percent.threeQuarter()));
    }

    @ParameterizedTest
    @CsvSource({"260,255", "-10, 0"})
    void clampRgbRange_outOfRange_isClamped(int in, int out) {
        assertThat(Color.clampRgbRange(in)).isEqualTo(out);
    }

    @Test
    void clampRgbRange_inRange_isNotChanged() {
        assertThat(Color.clampRgbRange(200)).isEqualTo(200);
    }
}
