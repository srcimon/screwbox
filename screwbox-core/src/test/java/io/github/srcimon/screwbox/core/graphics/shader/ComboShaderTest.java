package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ComboShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline() {
        var result = new ComboShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE)).apply(source, Percent.zero());
        TestUtil.verifyIsSameImage(result, "shader/apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline.png");
    }

    @Test
    void newInstance_twoNonAnimatedShaders_isNotAnimated() {
        var comboShader = new ComboShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE));
        assertThat(comboShader.isAnimated()).isFalse();
    }

    @Test
    void newInstance_oneAnimatedShaders_isAnimated() {
        var comboShader = new ComboShader(new SizeIncreaseShader(2), new WaterDistortionShader(1, 2));
        assertThat(comboShader.isAnimated()).isTrue();
    }

    @Test
    void newInstance_multipleShaders_hasCombinedCacheKey() {
        var comboShader = new ComboShader(new SizeIncreaseShader(2), new WaterDistortionShader(1, 2));
        assertThat(comboShader.cacheKey()).isEqualTo("combo-shader-size-expansion-2-WaterDistortionShader-1-2.0-");
    }
}
