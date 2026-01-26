package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;


import java.awt.*;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;


class BlendShaderTest {

    private static final Image SOURCE = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_innerShaderNotNull_returnsBlendedImage() {
        var blendShader = new BlendShader(new SilhouetteShader(Color.BLUE));
        var result = blendShader.apply(SOURCE, Percent.of(0.3));
        new Frame(result, Duration.none()).exportPng("apply_innerShaderNotNull_returnsBlendedImage.png");
        verifyIsSameImage(result, "shader/apply_innerShaderNotNull_returnsBlendedImage.png");
    }
}
