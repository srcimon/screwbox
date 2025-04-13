package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PolygonDrawOptionsTest {

    @Test
    void strokeWidth_widthZero_throwsException() {
        var options = PolygonDrawOptions.filled(Color.RED);

        assertThatThrownBy(() -> options.strokeWidth(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("stroke width must be positive");
    }
}