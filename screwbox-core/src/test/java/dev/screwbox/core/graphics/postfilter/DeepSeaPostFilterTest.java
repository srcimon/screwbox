package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeepSeaPostFilterTest extends PostFilterTest {

    @Test
    void newInstance_noBubbles_throwsException() {
        assertThatThrownBy(() -> new DeepSeaPostFilter(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("bubble count must be positive (actual value: 0)");
    }

    @Test
    void applyDeepSeaPostFilter_validInput_isCorrect() {
        var filter = new DeepSeaPostFilter(50);
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyDeepSeaPostFilter_validInput_isCorrect.png");
    }
}
