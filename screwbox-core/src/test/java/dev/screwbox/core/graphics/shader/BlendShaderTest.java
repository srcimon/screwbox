package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;


class BlendShaderTest {

    private static final Image SOURCE = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_noProgress_returnsOriginal() {
        var blendShader = new BlendShader(new SilhouetteShader(Color.BLUE));
        var result = blendShader.apply(SOURCE, Percent.zero());
        verifyIsSameImage(result, "shader/apply_noProgress_returnsOriginal.png");
    }

    @Test
    void apply_fullProgress_returnsInnerShaderResult() {
        var blendShader = new BlendShader(new SilhouetteShader(Color.BLUE));
        var result = blendShader.apply(SOURCE, Percent.max());
        verifyIsSameImage(result, "shader/apply_fullProgress_returnsInnerShaderResult.png");
    }
}
