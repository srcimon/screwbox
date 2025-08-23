package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineDrawOptionsTest {

    @Test
    void strokeWidth_zero_throwsException() {
        LineDrawOptions options = LineDrawOptions.color(Color.WHITE);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive (actual value: 0)");
    }
}
