package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static io.github.srcimon.screwbox.core.test.TestUtil.verifyIsSameImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CombinedShaderTest {

    private static final Image source = Frame.fromFile("tile.bmp").image();

    @Test
    void newInstance_onlyOneShader_throwsException() {
        final var greyscaleShader = new GreyscaleShader();
        assertThatThrownBy(() -> new CombinedShader(greyscaleShader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("combined shader needs at least two sub shaders");
    }

    @Test
    void apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline() {
        var result = new CombinedShader(new SizeIncreaseShader(2), new OutlineShader(Color.BLUE)).apply(source, Percent.zero());
        verifyIsSameImage(result, "shader/apply_increaseSizeAndOutline_firstIncreasesSizeAndThenAddsOutline.png");
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
        assertThat(comboShader.cacheKey()).isEqualTo("combined-shader-size-increase-2-2-DistortionShader-1-2.0-2.0");
    }
}
