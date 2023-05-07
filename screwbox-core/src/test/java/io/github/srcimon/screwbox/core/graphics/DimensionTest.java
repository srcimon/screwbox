package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

class DimensionTest {

    @Test
    void square_returnsNewInstance() {
        Dimension square = Dimension.square(4);

        assertThat(square).isEqualTo(Dimension.of(4, 4));
    }

    @Test
    void definedBy_twoValidOffsets_returnsNewInstance() {
        Dimension result = Dimension.definedBy(Offset.at(1, 4), Offset.at(8, 3));

        assertThat(result).isEqualTo(Dimension.of(7, 1));
    }

    @Test
    void definedBy_sameOffsets_returnsDimensionNone() {
        Dimension result = Dimension.definedBy(Offset.at(1, 4), Offset.at(1, 4));

        assertThat(result).isEqualTo(Dimension.none());
    }

    @Test
    void pixelCount_returnsCountOfPixels() {
        Dimension dimension = Dimension.of(640, 480);

        assertThat(dimension.pixelCount()).isEqualTo(307200);
    }

    @Test
    void testSort_byPixelCount() {
        Dimension mediumRes = Dimension.of(1280, 1024);
        Dimension highRes = Dimension.of(4096.0, 3072.0);
        Dimension lowRes = Dimension.of(640, 480);

        var dimensions = new ArrayList<>(of(mediumRes, highRes, lowRes));

        Collections.sort(dimensions);

        assertThat(dimensions).containsSequence(lowRes, mediumRes, highRes);
    }

}
