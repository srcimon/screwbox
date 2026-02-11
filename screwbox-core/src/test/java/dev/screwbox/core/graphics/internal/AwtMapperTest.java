package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import org.junit.jupiter.api.Test;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AwtMapperTest {

    @Test
    void toAwtColor_mapsAllRedGreenAndBlueAndAlpha() {
        Color color = Color.rgb(20, 40, 60);

        var awtColor = AwtMapper.toAwtColor(color);

        assertThat(awtColor.getRed()).isEqualTo(20);
        assertThat(awtColor.getGreen()).isEqualTo(40);
        assertThat(awtColor.getBlue()).isEqualTo(60);
        assertThat(awtColor.getAlpha()).isEqualTo(255);
    }

    @Test
    void toAwtColor_mapsOpacity() {
        Color color = Color.rgb(20, 40, 60, Percent.half());

        var awtColor = AwtMapper.toAwtColor(color);

        assertThat(awtColor.getRed()).isEqualTo(20);
        assertThat(awtColor.getGreen()).isEqualTo(40);
        assertThat(awtColor.getBlue()).isEqualTo(60);
        assertThat(awtColor.getAlpha()).isEqualTo(127);
    }

    @Test
    void toPath_validPoint_createsPath() {
        GeneralPath path = AwtMapper.toPath(List.of(Offset.at(10, 2), Offset.at(19, 2), Offset.at(21, 3)));

        PathIterator iterator = path.getPathIterator(null);
        float[] coords = new float[6];

        assertThat(iterator.isDone()).isFalse();
        assertThat(iterator.currentSegment(coords)).isEqualTo(PathIterator.SEG_MOVETO);
        assertThat(coords[0]).isEqualTo(10);
        assertThat(coords[1]).isEqualTo(2);

        iterator.next();

        assertThat(iterator.isDone()).isFalse();
        assertThat(iterator.currentSegment(coords)).isEqualTo(PathIterator.SEG_LINETO);
        assertThat(coords[0]).isEqualTo(10);
        assertThat(coords[1]).isEqualTo(2);

        iterator.next();

        assertThat(iterator.isDone()).isFalse();
        assertThat(iterator.currentSegment(coords)).isEqualTo(PathIterator.SEG_LINETO);
        assertThat(coords[0]).isEqualTo(19);
        assertThat(coords[1]).isEqualTo(2);

        iterator.next();

        assertThat(iterator.isDone()).isFalse();
        assertThat(iterator.currentSegment(coords)).isEqualTo(PathIterator.SEG_LINETO);
        assertThat(coords[0]).isEqualTo(21);
        assertThat(coords[1]).isEqualTo(3);

        iterator.next();

        assertThat(iterator.isDone()).isTrue();
    }
}
