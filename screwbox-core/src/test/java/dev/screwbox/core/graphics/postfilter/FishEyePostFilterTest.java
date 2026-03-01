package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.SpriteBundle;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;


class FishEyePostFilterTest extends PostFilterTest {

    @Test
    void applyFishEyePostFilter_blueBackground_hasBlueFrame() {
        var filter = new FishEyePostFilter(10, -0.3);
        Image sourceImage = SpriteBundle.SHADER_PREVIEW.get().singleImage();

        PostProcessingContext context = new PostProcessingContext(Color.BLUE, Duration.ofMillis(10), viewport);

        filter.apply(sourceImage, target, context);

        assertThat(Frame.fromImage(targetImage).hasIdenticalPixels(Frame.fromFile("postfilter/applyFishEyePostFilter_blueBackground_hasBlueFrame.png")));
    }

}
