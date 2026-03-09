package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class CrtMonitorPostFilterTest extends PostFilterTest {

    @Test
    void applyCrtMonitorPostFilter_validInput_isCorrect() {
        var filter = new CrtMonitorPostFilter();
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyCrtMonitorPostFilter_validInput_isCorrect.png");
    }
}
