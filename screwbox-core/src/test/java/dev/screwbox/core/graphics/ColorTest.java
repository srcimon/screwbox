package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

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
            .hasMessage("invalid red color value 0 to 255 (actual value: -10)");
    }

    @Test
    void newInstance_tooHighInvalidColor_throwsException() {
        assertThatThrownBy(() -> Color.rgb(0, 200, 260))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("invalid blue color value 0 to 255 (actual value: 260)");
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
    @ValueSource(ints = {15538175, 123199, 130004})
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

        assertThat(alpha).isEqualTo(255);
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

    @ParameterizedTest
    @CsvSource({
        "#5f01e2, 107",
        "#020682, 46"
    })
    void brightness_colorIn_brightnessOut(String hex, int brightness) {
        assertThat(Color.hex(hex).brightness()).isEqualTo(brightness);
    }

    @Test
    void difference_sameColor_isZero() {
        assertThat(Color.RED.difference(Color.RED)).isZero();
    }

    @Test
    void difference_sameColorButDistinctOpacity_isZero() {
        assertThat(Color.RED.difference(Color.RED.opacity(Percent.half()))).isZero();
    }

    @Test
    void difference_otherNull_throwsException() {
        assertThatThrownBy(() -> Color.RED.difference(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("other color must not be null");
    }

    @ParameterizedTest
    @CsvSource({
        "#ff0000, #00ff00, 360.62",
        "#025f12, #bd88fd, 303.11",
        "#4cc4ab, #9bd287, 87.94"
    })
    void difference_otherIsDifferent_returnsDistance(String color, String other, double distance) {
        double result = Color.hex(color).difference(Color.hex(other));
        assertThat(result).isEqualTo(distance, offset(0.01));
    }

    @Test
    void isVisible_transparent_isFalse() {
        assertThat(Color.TRANSPARENT.isVisible()).isFalse();
    }

    @Test
    void isVisible_orange_isTrue() {
        assertThat(Color.ORANGE.isVisible()).isTrue();
    }

    @Test
    void adjustBrightness_redDarkenMax_isBlack() {
        var adjustedColor = Color.RED.adjustBrightness(-1.0);
        assertThat(adjustedColor).isEqualTo(Color.BLACK);
    }

    @Test
    void adjustBrightness_redBrightenMax_isStillRed() {
        var adjustedColor = Color.RED.adjustBrightness(1.0);
        assertThat(adjustedColor).isEqualTo(Color.RED);
    }

    @Test
    void adjustBrightness_noChange_isSameColor() {
        var adjustedColor = Color.RED.adjustBrightness(0.0);
        assertThat(adjustedColor).isEqualTo(Color.RED);
    }

    @Test
    void adjustBrightness_lowerBrightness_isDarker() {
        var color = Color.rgb(200, 100, 40);
        var adjustedColor = color.adjustBrightness(-0.5);

        assertThat(color.brightness()).isGreaterThan(adjustedColor.brightness());
        assertThat(adjustedColor).isEqualTo(Color.rgb(100, 50, 20));
    }

    @Test
    void adjustBrightness_outOfRange_throwsException() {
        assertThatThrownBy(() -> Color.RED.adjustBrightness(5.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("adjustment must be between -1.0 and 1.0 (actual value: 5.0)");
    }
}
