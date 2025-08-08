package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;

class SilhouetteShaderTest {

    private static final Image SOURCE = Frame.fromFile("transparent.png").image();

    @Test
    void apply_colorRed_drawsRedSilhouette() {
        var result = new SilhouetteShader(Color.RED).apply(SOURCE, Percent.zero());

        verifyIsSameImage(result, "shader/apply_colorRed_drawsRedSilhouette.png");
    }

}
