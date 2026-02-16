package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistortionImageFilterTest {

    @Test
    void applyFilter_validInput_createsWaveEffectOnOutput() {
        var input = ImageOperations.cloneImage(SpriteBundle.BOX.get().singleImage());
        final var filterConfig = new DistortionImageFilter.DistortionConfig(2012, 4, 0.3, 0.2, Offset.origin());
        var filter = new DistortionImageFilter(filterConfig);

        var result = filter.apply(input);

        Frame reference = Frame.fromFile("filter/applyFilter_validInput_createsWaveEffectOnOutput.png");
        assertThat(Frame.fromImage(result).hasIdenticalPixels(reference)).isTrue();
    }
}
