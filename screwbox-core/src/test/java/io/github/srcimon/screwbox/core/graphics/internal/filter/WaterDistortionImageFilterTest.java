package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WaterDistortionImageFilterTest {

    @Test
    void applyFilter_validInput_createsWaveEffectOnOutput() {
        var input = ImageUtil.toBufferedImage(SpriteBundle.BOX_STRIPED.get().singleImage());
        final var filterConfig = new WaterDistortionImageFilter.WaterDistortionConfig(2012, 4, 0.3, 0.2, Offset.origin());
        var filter = new WaterDistortionImageFilter(input, filterConfig);

        var result = ImageUtil.applyFilter(input, filter);

        Frame reference = Frame.fromFile("filter/applyFilter_validInput_createsWaveEffectOnOutput.png");
        assertThat(Frame.fromImage(result).listPixelDifferences(reference)).isEmpty();
    }

    @Test
    void newInstance_inputIsNull_throwsException() {
        final var filterConfig = new WaterDistortionImageFilter.WaterDistortionConfig(2012, 4, 0.3, 0.2, Offset.origin());

        assertThatThrownBy(() -> new WaterDistortionImageFilter(null, filterConfig))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("source image must not be null");
    }
}
