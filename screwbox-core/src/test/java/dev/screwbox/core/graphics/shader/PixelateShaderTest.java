package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PixelateShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void newInstance_pixelSizeInvalid_throwsException() {
        assertThatThrownBy(() -> new PixelateShader(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pixel size must be in range from 1 to 32");
    }

    @Test
    void apply_pixelSizeFour_createsImage() {
        var result = new PixelateShader(4).apply(source, Percent.max());
        verifyIsSameImage(result, "shader/apply_pixelSizeFour_createsImage.png");
    }

    @Test
    void apply_pixelSizeTwo_createsImage() {
        var result = new PixelateShader(2).apply(source, Percent.max());
        verifyIsSameImage(result, "shader/apply_pixelSizeTwo_createsImage.png");
    }
}
