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

    @Test
    void replaceColor_replacesOldColorWithNewOne() {
        Frame frame = Sprite.fromFile("tile.bmp").singleFrame();
        Color oldColor = frame.colorAt(4, 4);

        frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = frame.colorAt(4, 4);
        assertThat(oldColor).isNotEqualTo(newColor);
        assertThat(newColor).isEqualTo(Color.BLUE);
    }

}
