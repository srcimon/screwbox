package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class WarpPostFilterTest extends PostFilterTest {

    @Test
    void applyWarpPostFilter_atSpecificTime_returnsCorrectImage() {
        var filter = new WarpPostFilter(Percent.of(0.4));
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyWarpPostFilter_atSpecificTime_returnsCorrectImage.png");
    }
}
