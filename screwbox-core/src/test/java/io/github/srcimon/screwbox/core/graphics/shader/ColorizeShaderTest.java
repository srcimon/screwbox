package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.test.TestUtil;
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
