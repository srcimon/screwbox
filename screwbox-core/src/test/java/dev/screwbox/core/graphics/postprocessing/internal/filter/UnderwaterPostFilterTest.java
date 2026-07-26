package dev.screwbox.core.graphics.postprocessing.internal.filter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.postprocessing.filter.PostProcessingContext;
import dev.screwbox.core.graphics.postprocessing.filter.UnderwaterPostFilter;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class UnderwaterPostFilterTest extends PostFilterTest {

    @Test
    void applyUnderwaterPostFilter_atSpecificTime_returnsCorrectImage() {
        var filter = new UnderwaterPostFilter();
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport, 1.0);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyUnderwaterPostFilter_atSpecificTime_returnsCorrectImage.png");
    }
}
