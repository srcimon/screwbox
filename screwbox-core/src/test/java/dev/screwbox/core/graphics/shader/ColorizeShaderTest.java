package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

class ColorizeShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_halfProcessed_appliesColorChange() {
        var result = new ColorizeShader(Color.RED).apply(source, Percent.of(0.0));
        TestUtil.verifyIsSameImage(result, "shader/apply_halfProcessed_appliesColorChange.png");
    }

}
