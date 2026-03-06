package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class WavePostFilterTest extends PostFilterTest {

    @Test
    void applyWavePostFilter_atSpecificTime_returnsCorrectImage() {
        var filter = new WavePostFilter(1, Duration.ofMillis(150), 10, Percent.of(0.4));
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyWavePostFilter_atSpecificTime_returnsCorrectImage.png");
    }
}
