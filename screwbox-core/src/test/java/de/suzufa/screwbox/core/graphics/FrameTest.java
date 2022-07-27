package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FrameTest {

    @Test
    void colorAt_returnsColorAtPoision() {
        Frame frame = Sprite.fromFile("tile.bmp").singleFrame();

        Color color = frame.colorAt(Dimension.of(4, 4));

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

}
