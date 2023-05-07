package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.github.srcimon.screwbox.core.graphics.Dimension.square;
import static io.github.srcimon.screwbox.core.graphics.Offset.origin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FrameTest {

    Frame frame;

    @BeforeEach
    void beforeEach() {
        frame = Frame.fromFile("tile.bmp");
    }

    @ParameterizedTest
    @CsvSource({"-1,1", "100,1", "4,-2", "4,44"})
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
    void scaled_validScale_returnsScaledFrame() {
        Frame scaled = frame.scaled(3);

        assertThat(scaled.size()).isEqualTo(square(48));
    }

    @Test
    void scaled_invalidScale_throwsException() {
        assertThatThrownBy(() -> frame.scaled(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scaled image is size is invalid");
    }

    @Test
    void fromFile_fileIsImage_returnsFrame() {
        Frame frame = Frame.fromFile("tile.bmp");
        assertThat(frame.size()).isEqualTo(Dimension.square(16));
    }

    @ParameterizedTest
    @CsvSource({"-1,1,4,4", "100,1,4,4", "4,-2,5,20", "4,4,13,1", "4,0,14,30"})
    void extractArea_outOfBounds_throwsException(int x, int y, int width, int height) {
        Offset offset = Offset.at(x, y);
        Dimension size = Dimension.of(width, height);

        assertThatThrownBy(() -> frame.extractArea(offset, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("given offset and size are out offrame bounds");
    }

    @Test
    void extractArea_inBounds_returnsextractArea() {
        Offset offset = Offset.at(4, 8);
        Dimension size = Dimension.of(2, 7);

        Frame result = frame.extractArea(offset, size);

        assertThat(result.size()).isEqualTo(size);
        assertThat(result.colorAt(origin())).isEqualTo(frame.colorAt(offset));
    }

    @Test
    void cropHorizontal_noTransparentPixels_doesntCrop() {
        var result = frame.cropHorizontal();

        assertThat(result).isNotEqualTo(frame);
        assertThat(result.size()).isEqualTo(frame.size());
        assertThat(result.duration()).isEqualTo(frame.duration());
    }

    @Test
    void cropHorizontal_hasTransparentPixels_cropsImage() {
        var input = Frame.fromFile("transparent.png");

        var result = input.cropHorizontal();

        assertThat(result.size()).isEqualTo(Dimension.of(12, 16));
        assertThat(result.duration()).isEqualTo(frame.duration());
        assertThat(result.colorAt(0, 0)).isEqualTo(Color.TRANSPARENT);
        assertThat(result.colorAt(0, 3)).isEqualTo(Color.rgb(102, 57, 49));
    }
}
