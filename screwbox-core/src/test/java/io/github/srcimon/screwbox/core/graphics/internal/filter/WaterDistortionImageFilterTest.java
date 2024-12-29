package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WaterDistortionImageFilterTest {

    @Test
    void applyFilter_validInput_createsWaveEffectOnOutput() {
        var input = ImageUtil.toBufferedImage(SpriteBundle.BOX_STRIPED.get().singleImage());
        var filter = new WaterDistortionImageFilter(input, 2012);

        var result = ImageUtil.applyFilter(input, filter);

        Frame reference = Frame.fromFile("filter/applyFilter_validInput_createsWaveEffectOnOutput.png");
        assertThat(Frame.fromImage(result).listPixelDifferences(reference)).isEmpty();
    }

    @Test
    void newInstance_inputIsNull_throwsException() {
        assertThatThrownBy(() -> new WaterDistortionImageFilter(null, 10))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("source image must not be null");
    }
}
