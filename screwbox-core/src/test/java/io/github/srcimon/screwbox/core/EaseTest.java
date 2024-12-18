package io.github.srcimon.screwbox.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
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
    })
    void applyOn_inputValid_returnsUpdatedOutput(String modeName, double in, double out) {
        Percent input = Percent.of(in);

        var output = Ease.valueOf(modeName).applyOn(input);

        assertThat(output.value()).isEqualTo(out, offset(0.1));
    }
}
