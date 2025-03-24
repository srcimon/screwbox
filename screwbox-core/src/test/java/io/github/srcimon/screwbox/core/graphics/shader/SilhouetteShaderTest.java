package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static io.github.srcimon.screwbox.core.test.TestUtil.verifyIsSameImage;

class SilhouetteShaderTest {

    private static final Image source = Frame.fromFile("transparent.png").image();

    @Test
    void apply_colorRed_drawsRedSilhouette() {
        var result = new SilhouetteShader(Color.RED).apply(source, Percent.zero());

        verifyIsSameImage(result, "shader/apply_colorRed_drawsRedSilhouette.png");
    }

}
