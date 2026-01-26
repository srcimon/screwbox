package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Size;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExpandShaderTest {

    private static final Image SOURCE = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_sameIncreaseXandY_increasesBoth() {
        var result = new ExpandShader(4).apply(SOURCE, Percent.zero());

        var resultFrame = Frame.fromImage(result);
        assertThat(resultFrame.size()).isEqualTo(Size.of(24, 24));
    }

    @Test
    void apply_increaseXonly_doesntChangeY() {
        var result = new ExpandShader(4, 0).apply(SOURCE, Percent.zero());

        var resultFrame = Frame.fromImage(result);
        assertThat(resultFrame.size()).isEqualTo(Size.of(24, 16));
    }

    @Test
    void newInstance_zeroIncrease_throwsException() {
        assertThatThrownBy(() -> new ExpandShader(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("at least one axis should be size increased");
    }

    @Test
    void newInstance_xIncreaseInvalid_throwsException() {
        assertThatThrownBy(() -> new ExpandShader(-2, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("only size increase from 1 to 32 is supported (actual value: -2)");
    }

    @Test
    void newInstance_yIncreaseInvalid_throwsException() {
        assertThatThrownBy(() -> new ExpandShader(2, 33))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("only size increase from 1 to 32 is supported (actual value: 33)");
    }

}
