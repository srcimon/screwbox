package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class UnderwaterPostFilterTest extends PostFilterTest {

    @Test
    void applyUnderwaterPostFilter_atSpecificTime_returnsCorrectImage() {
        var filter = new UnderwaterPostFilter();
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyUnderwaterPostFilter_atSpecificTime_returnsCorrectImage.png");
    }
}
