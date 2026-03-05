package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HeatHazePostFilterTest extends PostFilterTest {

    @Test
    void newInstance_segmentHeightNegative_throwsException() {
        final Duration interval = Duration.oneSecond();
        assertThatThrownBy(() -> new HeatHazePostFilter(interval, -2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("segment height must be in range 2 to 48 (actual value: -2)");
    }

    @Test
    void applyHeatHazePostFilter_validInput_isCorrect() {
        var filter = new HeatHazePostFilter();
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(600), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyHeatHazePostFilter_validInput_isCorrect.png");
    }
}
