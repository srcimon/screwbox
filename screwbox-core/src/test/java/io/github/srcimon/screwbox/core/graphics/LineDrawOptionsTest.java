package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static io.github.srcimon.screwbox.core.graphics.LineDrawOptions.color;
import static org.assertj.core.api.Assertions.assertThat;

class LineDrawOptionsTest {

    @Test
    void testEquals() {
        assertThat(color(Color.RED).strokeWidth(2))
                .isNotEqualTo(color(Color.BLUE).strokeWidth(2))
                .isNotEqualTo(color(Color.RED).strokeWidth(1))
                .isEqualTo(color(Color.RED).strokeWidth(2));
    }

    @Test
    void toString_returnsInformationOnTheDrawing() {
        var options = color(Color.RED).strokeWidth(2);

        assertThat(options).hasToString("LineDrawOptions{color=Color [r=255, g=0, b=0, opacity=1.0], strokeWidth=2}");
    }
}
