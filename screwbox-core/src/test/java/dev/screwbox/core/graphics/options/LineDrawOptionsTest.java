package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineDrawOptionsTest {

    @Test
    void strokeWidth_zero_throwsException() {
        LineDrawOptions options = LineDrawOptions.color(Color.WHITE);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive (actual value: 0)");
    }

    @Test
    void newInstance_everythingConfigured_hasValues() {
        LineDrawOptions options = LineDrawOptions.color(Color.WHITE)
                .strokeWidth(9)
                .drawOrder(20);

        assertThat(options.color()).isEqualTo(Color.WHITE);
        assertThat(options.strokeWidth()).isEqualTo(9);
        assertThat(options.drawOrder()).isEqualTo(20);
    }
}
