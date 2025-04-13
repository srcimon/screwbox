package dev.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SizeTest {

    @Test
    void of_negativeWidth_throwsException() {
        assertThatThrownBy(() -> Size.of(-4, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("width must be positive");
    }

    @Test
    void of_negativeHeight_throwsException() {
        assertThatThrownBy(() -> Size.of(4, -4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("height must be positive");
    }

    @Test
    void square_returnsNewInstance() {
        Size square = Size.square(4);

        assertThat(square).isEqualTo(Size.of(4, 4));
    }

    @Test
    void definedBy_twoValidOffsets_returnsNewInstance() {
        Size result = Size.definedBy(Offset.at(1, 4), Offset.at(8, 3));

        assertThat(result).isEqualTo(Size.of(7, 1));
    }

    @Test
    void definedBy_sameOffsets_returnsNone() {
        Size result = Size.definedBy(Offset.at(1, 4), Offset.at(1, 4));

        assertThat(result).isEqualTo(Size.none());
    }

    @Test
    void pixelCount_returnsCountOfPixels() {
        Size size = Size.of(640, 480);

        assertThat(size.pixelCount()).isEqualTo(307200);
    }

    @Test
    void testSort_byPixelCount() {
        Size mediumRes = Size.of(1280, 1024);
        Size highRes = Size.of(4096.0, 3072.0);
        Size lowRes = Size.of(640, 480);

        var dimensions = new ArrayList<>(of(mediumRes, highRes, lowRes));

        Collections.sort(dimensions);

        assertThat(dimensions).containsSequence(lowRes, mediumRes, highRes);
    }

    @Test
    void allPixels_smallSize_containsEveryPixel() {
        List<Offset> pixels = Size.of(3, 2).allPixels();

        assertThat(pixels).containsExactly(
                Offset.at(0, 0),
                Offset.at(0, 1),
                Offset.at(1, 0),
                Offset.at(1, 1),
                Offset.at(2, 0),
                Offset.at(2, 1));
    }

    @Test
    void isValid_positiveWidthAndHeight_isTrue() {
        assertThat(Size.of(1, 129).isValid()).isTrue();
    }

    @Test
    void isValid_widthOrHeightZero_isFalse() {
        assertThat(Size.of(0, 129).isValid()).isFalse();
        assertThat(Size.of(1, 0).isValid()).isFalse();
    }

    @Test
    void expand_positiveValue_makesBigger() {
        Size size = Size.of(0, 129);
        assertThat(size.expand(4)).isEqualTo(Size.of(4, 133));
    }
}
