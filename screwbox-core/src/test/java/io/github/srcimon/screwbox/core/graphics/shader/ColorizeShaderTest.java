package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ColorizeShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_halfProcessed_appliesColorChange() {
        var result = new ColorizeShader(Color.RED).apply(source, Percent.of(0.0));
        verifyIsSame(result, "apply_halfProcessed_appliesColorChange.png");
    }

    private void verifyIsSame(final Image result, final String file) {
        Frame resultFrame = Frame.fromImage(result);
        Frame fileFrame = Frame.fromFile("shader/" + file);
        assertThat(fileFrame.listPixelDifferences(resultFrame)).isEmpty();
    }
}
