package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlurImageFilterTest {

    @Test
    void newInstance_radiusTooSmall_throwsException() {
        assertThatThrownBy(() -> new BlurImageFilter(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("radius must be in range 1 to 6");
    }

    @Test
    void newInstance_radiusTooBig_throwsException() {
        assertThatThrownBy(() -> new BlurImageFilter(12))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("radius must be in range 1 to 6");
    }

    @Test
    void apply_validSourceImage_returnsBluredResult() {
        BufferedImage sourceImage = ImageOperations.toBufferedImage(Frame.fromFile("tile.bmp").image());

        var result = new BlurImageFilter(4).apply(sourceImage);

        TestUtil.verifyIsSameImage(result, "filter/apply_validSourceImage_returnsBluredResult.png");
    }
}
