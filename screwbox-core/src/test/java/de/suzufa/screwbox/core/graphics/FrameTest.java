package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FrameTest {

    @Test
    void colorAt_outOfBounds_throwsException() {
        Frame frame = Sprite.invisible().singleFrame();

        assertThatThrownBy(() -> frame.colorAt(Dimension.of(40, 45)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dimension is out of bounds: 40:45");
    }

    @Test
    void colorAt_inBounds_returnsColor() {
        Frame frame = Sprite.fromFile("tile.bmp").singleFrame();

        Color color = frame.colorAt(Dimension.of(4, 4));

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

}
