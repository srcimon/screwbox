package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

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
    void toColor_mapsAllRedGreenAndBlueAndAlpha() {
        var awtColor = new java.awt.Color(20, 40, 60, 127);

        var color = AwtMapper.toColor(awtColor);
        assertThat(color.r()).isEqualTo(20);
        assertThat(color.g()).isEqualTo(40);
        assertThat(color.b()).isEqualTo(60);
        assertThat(color.opacity().value()).isEqualTo(0.5, offset(0.1));
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
}
