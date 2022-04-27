package de.suzufa.screwbox.core.graphics.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.suzufa.screwbox.core.graphics.Color;

class AwtMapperTest {

    @Test
    void toAwtColor_mapsAllredGreenAndBlue() {
        java.awt.Color awtColor = AwtMapper.toAwtColor(Color.rgb(20, 40, 60));
        assertThat(awtColor.getRed()).isEqualTo(20);
        assertThat(awtColor.getGreen()).isEqualTo(40);
        assertThat(awtColor.getBlue()).isEqualTo(60);
    }

}
