package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnderwaterShaderTest {

    @Test
    void apply_someDistortion_createsUnderwaterEffect() {
        Image image = SpriteBundle.BOX.get().singleImage();

        var result = new UnderwaterShader(10, 4).apply(image, Percent.half());

        TestUtil.verifyIsSameImage(result, "shader/apply_someDistortion_createsUnderwaterEffect.png");
    }

    @Test
    void newInstance_noZoom_throwsException() {
        assertThatThrownBy(() -> new UnderwaterShader(0, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("zoom must be in range 1 to 100");
    }

    @Test
    void newInstance_tooMuchDistortion_throwsException() {
        assertThatThrownBy(() -> new UnderwaterShader(8, 400))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("distortion must be in range 1 to 32");
    }
}
