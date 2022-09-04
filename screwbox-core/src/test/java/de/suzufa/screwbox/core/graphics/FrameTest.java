package de.suzufa.screwbox.core.graphics;

import static de.suzufa.screwbox.core.graphics.Dimension.square;
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
        frame = Frame.fromFile("tile.bmp");
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
        Color color = frame.colorAt(Offset.at(4, 4));

        assertThat(color).isEqualTo(Color.rgb(199, 155, 119));
    }

    @Test
    void replaceColor_doenstReplaceColorInOldFrame() {
        Color oldColor = frame.colorAt(4, 4);

        frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = frame.colorAt(4, 4);
        assertThat(oldColor).isEqualTo(newColor);
    }

    @Test
    void replaceColor_replaceColorInNewFrame() {
        Color oldColor = frame.colorAt(4, 4);

        Frame newFrame = frame.replaceColor(oldColor, Color.BLUE);

        Color newColor = newFrame.colorAt(4, 4);
        assertThat(newColor).isNotEqualTo(oldColor).isEqualTo(Color.BLUE);
    }

    @Test
    void replaceColor_doenstReplaceOtherColors() {
        frame.replaceColor(Color.BLACK, Color.BLUE);

        assertThat(frame.colorAt(4, 4)).isNotEqualTo(Color.BLUE);
    }

    @Test
    void scaled_returnsScaledFrame() {
        Frame scaled = frame.scaled(3);

        assertThat(scaled.size()).isEqualTo(square(48));
    }

    @Test
    void fromFile_fileIsImage_returnsFrame() {
        Frame frame = Frame.fromFile("tile.bmp");
        assertThat(frame.size()).isEqualTo(Dimension.square(16));
    }
}
