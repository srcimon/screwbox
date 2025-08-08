package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static dev.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;

class ColorPaletteShaderTest {

    private static final Image SOURCE = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_smallPalette_reducesImageToPalette() {
        var result = new ColorPaletteShader(Set.of(Color.RED, Color.BLUE, Color.GREEN, Color.DARK_BLUE)).apply(SOURCE, Percent.zero());
        verifyIsSameImage(result, "shader/apply_smallPalette_reducesImageToPalette.png");
    }

    @Test
    void cacheKey_containsDuplicateColor_doesntContainDuplicateColor() {
        var shader = new ColorPaletteShader(Set.of(Color.RED, Color.BLUE, Color.GREEN, Color.GREEN.opacity(0.2), Color.DARK_BLUE));
        assertThat(shader.cacheKey()).isEqualTo("ColorPaletteShader-#000080-#0000ff-#00ff00-#ff0000");
    }
}
