package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class FishEyePostFilterTest extends PostFilterTest {

    @Test
    void newInstance_gridSizeOutOfRange_throwsException() {
        assertThatThrownBy(() -> new FishEyePostFilter(0, -0.4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("grid size must be in range 2 to 128 (actual value: 0)");
    }

    @Test
    void newInstance_strengthOutOfRange_throwsException() {
        assertThatThrownBy(() -> new FishEyePostFilter(4, 4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("strength must be in range -1 to 1 (actual value: 4.0)");
    }

    @Test
    void applyFishEyePostFilter_blueBackground_hasBlueFrame() {
        var filter = new FishEyePostFilter(10, -0.3);
        var context = new PostProcessingContext(Color.BLUE, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        assertThat(Frame.fromImage(targetImage).hasIdenticalPixels(Frame.fromFile("postfilter/applyFishEyePostFilter_blueBackground_hasBlueFrame.png")));
    }

}
