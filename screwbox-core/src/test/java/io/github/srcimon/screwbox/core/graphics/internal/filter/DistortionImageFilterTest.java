package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistortionImageFilterTest {

    @Test
    void applyFilter_validInput_createsWaveEffectOnOutput() {
        var input = ImageOperations.toBufferedImage(SpriteBundle.BOX_STRIPED.get().singleImage());
        final var filterConfig = new DistortionImageFilter.DistortionConfig(2012, 4, 0.3, 0.2, Offset.origin());
        var filter = new DistortionImageFilter(input, filterConfig);

        var result = ImageOperations.applyFilter(input, filter);

        Frame reference = Frame.fromFile("filter/applyFilter_validInput_createsWaveEffectOnOutput.png");
        assertThat(Frame.fromImage(result).listPixelDifferences(reference)).isEmpty();
    }

    @Test
    void newInstance_inputIsNull_throwsException() {
        final var filterConfig = new DistortionImageFilter.DistortionConfig(2012, 4, 0.3, 0.2, Offset.origin());

        assertThatThrownBy(() -> new DistortionImageFilter(null, filterConfig))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("source image must not be null");
    }
}
