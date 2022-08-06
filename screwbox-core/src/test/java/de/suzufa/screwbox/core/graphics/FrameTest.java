package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FrameTest {

    Frame frame;

    @BeforeEach
    void beforeEach() {
        frame = Sprite.fromFile("tile.bmp").singleFrame();
    }

    @ParameterizedTest
    @CsvSource({ "-1,1", "100,1", "4,-2", "4,44" })
    void colorAt_outOfBounds_throwsException(int x, int y) throws Exception {
        assertThatThrownBy(() -> frame.colorAt(x, y))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Position is out of bounds: " + x + ":" + y);
    }

    @Test
    void colorAt_inBounds_returnsColorAtPoision() {
        Color color = frame.colorAt(Dimension.of(4, 4));

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

    @Test
    void replaceColor_replacesOldColorWithNewOne() {
        Color oldColor = frame.colorAt(4, 4);

        frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = frame.colorAt(4, 4);
        assertThat(oldColor).isNotEqualTo(newColor);
        assertThat(newColor).isEqualTo(Color.BLUE);
    }

    @Test
    void replaceColor_doenstReplaceOtherColors() {
        frame.replaceColor(Color.BLACK, Color.BLUE);

        assertThat(frame.colorAt(4, 4)).isNotEqualTo(Color.BLUE);
    }

}
