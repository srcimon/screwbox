package dev.screwbox.core;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

class EaseTest {
    @ParameterizedTest
    @CsvSource({
            "LINEAR_IN,0.2,0.2",
            "LINEAR_IN,0.8,0.8",
            "LINEAR_OUT,0.2,0.8",
            "LINEAR_OUT,0.8,0.2",
            "SINE_IN,0.2,0.31",
            "SINE_IN,0.4,0.58",
            "SINE_IN,0.8,0.95",
            "SINE_IN_OUT,0,0",
            "SINE_IN_OUT,0.5,1",
            "SINE_IN_OUT,0.3,0.65",
            "SINE_IN_OUT,1,0",
            "SPARKLE,0, 0",
            "SPARKLE,1, 0",
            "SPARKLE,0.34, 1",
            "SPARKLE,0.04, 1",
            "FLICKER,0.04, 0",
            "FLICKER,0.34, 0",
            "FLICKER,0, 1",
            "FLICKER,1, 1",
            "SIN_IN_OUT_TWICE,0,0",
            "SIN_IN_OUT_TWICE,1,0",
            "SIN_IN_OUT_TWICE,0.5,0",
            "SIN_IN_OUT_TWICE,0.25,1",
            "IN_PLATEAU_OUT,0,0",
            "IN_PLATEAU_OUT,0.05,0.5",
            "IN_PLATEAU_OUT,0.5,1",
            "IN_PLATEAU_OUT,0.95,0.5",
            "IN_PLATEAU_OUT,1,0",
            "IN_PLATEAU,0,0",
            "IN_PLATEAU,0.05,0.5",
            "IN_PLATEAU,0.4,1",
            "IN_PLATEAU,1,1",
            "PLATEAU_OUT,0,1",
            "PLATEAU_OUT,0.05,1",
            "PLATEAU_OUT,0.95,0.5",
            "PLATEAU_OUT,1,0",
            "PLATEAU_OUT_SLOW,0,1",
            "PLATEAU_OUT_SLOW,0.05,1",
            "PLATEAU_OUT_SLOW,0.95,0.25",
            "PLATEAU_OUT_SLOW,1,0",
            "SQUARE_IN,0,0",
            "SQUARE_IN,0.5,0.25",
            "SQUARE_IN,1,1",
            "SQUARE_OUT,1,0",
            "SQUARE_OUT,0.5,0.70",
            "SQUARE_OUT,1,0",
            "S_CURVE_IN,0,0",
            "S_CURVE_OUT,0,1",
            "S_CURVE_IN,1,1",
            "S_CURVE_OUT,1,0",
            "S_CURVE_IN,0.5,0.5",
            "S_CURVE_OUT,0.5,0.5",
            "S_CURVE_IN,0.25,0.15625",
            "S_CURVE_OUT,0.25,0.84375",
            "S_CURVE_IN,0.75,0.84375",
            "S_CURVE_OUT,0.75,0.15625",
    })
    void apply_On_inputValid_returnsUpdatedOutput(String modeName, double in, double out) {
        Percent input = Percent.of(in);

        var output = Ease.valueOf(modeName).applyOn(input);

        assertThat(output.value()).isEqualTo(out, offset(0.1));
    }


    @Test
    void createPreview_colorIsNull_throwsException() {
        final var size = Size.square(2);
        assertThatThrownBy(() -> Ease.LINEAR_IN.createPreview(null, size))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("color must not be null");
    }

    @Test
    void createPreview_sizeZero_throwsException() {
        final var size = Size.of(0, 10);

        assertThatThrownBy(() -> Ease.LINEAR_IN.createPreview(Color.RED, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("preview size must be valid");
    }

    @Test
    void createPreview_validInput_createsPreview() {
        Frame preview = Ease.LINEAR_IN.createPreview(Color.RED, Size.square(10));

        assertThat(preview.colors()).containsExactlyInAnyOrder(Color.TRANSPARENT, Color.RED);
        assertThat(preview.size()).isEqualTo(Size.square(10));
    }
}
