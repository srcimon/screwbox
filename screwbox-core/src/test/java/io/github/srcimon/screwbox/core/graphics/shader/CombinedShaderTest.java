package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class CombinedShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline() {
        var result = new CombinedShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE)).apply(source, Percent.zero());
        TestUtil.verifyIsSameImage(result, "shader/apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline.png");
    }

    @Test
    void newInstance_twoNonAnimatedShaders_isNotAnimated() {
        var comboShader = new CombinedShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE));
        assertThat(comboShader.isAnimated()).isFalse();
    }

    @Test
    void newInstance_oneAnimatedShaders_isAnimated() {
        var comboShader = new CombinedShader(new SizeIncreaseShader(2), new DistortionShader(1, 2, 3));
        assertThat(comboShader.isAnimated()).isTrue();
    }

    @Test
    void newInstance_multipleShaders_hasCombinedCacheKey() {
        var comboShader = new CombinedShader(new SizeIncreaseShader(2), new DistortionShader(1, 2, 2));
        assertThat(comboShader.cacheKey()).isEqualTo("combined-shader-size-increase-2-DistortionShader-1-2.0-2.0");
    }
}
