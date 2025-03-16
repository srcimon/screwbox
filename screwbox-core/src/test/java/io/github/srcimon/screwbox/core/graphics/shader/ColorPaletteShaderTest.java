package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Set;

import static io.github.srcimon.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;

class ColorPaletteShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();


    @Test
    void apply_smallPalette_reducesImageToPalette() {
        var result = new ColorPaletteShader(Set.of(Color.RED, Color.BLUE, Color.GREEN, Color.DARK_BLUE)).apply(source, Percent.zero());
        verifyIsSameImage(result, "shader/apply_smallPalette_reducesImageToPalette.png");
    }

    @Test
    void cacheKey_containsDuplicateColor_doesntContainDuplicateColor() {
        var shader = new ColorPaletteShader(Set.of(Color.RED, Color.BLUE, Color.GREEN, Color.GREEN.opacity(0.2), Color.DARK_BLUE));
        assertThat(shader.cacheKey()).isEqualTo("ColorPaletteShader-#000080-#0000ff-#00ff00-#ff0000");
    }
}
