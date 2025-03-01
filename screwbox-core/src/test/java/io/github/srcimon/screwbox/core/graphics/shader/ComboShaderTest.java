package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ComboShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline() {
        var result = new ComboShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE)).apply(source, Percent.zero());
        verifyIsSame(result, "apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline.png");
    }

    private void verifyIsSame(final Image result, final String file) {
        io.github.srcimon.screwbox.core.graphics.Frame resultFrame = io.github.srcimon.screwbox.core.graphics.Frame.fromImage(result);
        io.github.srcimon.screwbox.core.graphics.Frame fileFrame = Frame.fromFile("shader/" + file);
        assertThat(fileFrame.listPixelDifferences(resultFrame)).isEmpty();
    }
}
