package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class SizeTest {

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

}
