package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeeEyePostFilterTest extends PostFilterTest {

    @Test
    void newInstance_eyesTooSmall_throwsException() {
        assertThatThrownBy(() -> new BeeEyePostFilter(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("eye size must be in range 8 to 128 (actual value: 2)");
    }

    @Test
    void applyBeeEyePostFilter_validInput_isCorrect() {
        var filter = new BeeEyePostFilter(10);
        var context = new PostProcessingContext(Color.BLACK, Duration.ofMillis(10), viewport);

        filter.apply(source, target, context);

        verifyIsSameImage(targetImage, "postfilter/applyBeeEyePostFilter_validInput_isCorrect.png");
    }
}
