package dev.screwbox.core.graphics;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.shader.CombinedShader;
import dev.screwbox.core.graphics.shader.DistortionShader;
import dev.screwbox.core.graphics.shader.OutlineShader;
import dev.screwbox.core.graphics.shader.SizeIncreaseShader;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class ShaderSetupTest {

    @Test
    void newInstance_allValuesSet_isConfigured() {
        var setup = ShaderSetup
                .combinedShader(new SizeIncreaseShader(2), new OutlineShader(Color.RED))
                .duration(Duration.ofMillis(250))
                .ease(Ease.SINE_IN_OUT)
                .offset(Time.atNanos(1239139));

        assertThat(setup.duration()).isEqualTo(Duration.ofMillis(250));
        assertThat(setup.ease()).isEqualTo(Ease.SINE_IN_OUT);
        assertThat(setup.offset()).isEqualTo(Time.atNanos(1239139));
        assertThat(setup.shader()).isInstanceOf(CombinedShader.class);
        assertThat(setup.shader().cacheKey()).isEqualTo("combined-shader-size-increase-2-2-outline-#ff0000");
    }


    @Test
    void randomOffset_noOffsetSet_initializesOffset() {
        ShaderSetup shaderSetup = ShaderSetup.shader(new DistortionShader())
                .randomOffset();

        assertThat(shaderSetup.offset().isUnset()).isFalse();
    }

    @Test
    void createPreview_nonAnimatedPreview_hasOneFrame() {
        ShaderSetup shaderSetup = ShaderBundle.OUTLINE.get();
        Image source = SpriteBundle.DOT_RED.get().singleImage();

        var preview = shaderSetup.createPreview(source, 2);

        assertThat(preview.frameCount()).isOne();
    }

    @Test
    void createPreview_animatedPreviewWithTenFrames_hasTenFrame() {
        ShaderSetup shaderSetup = ShaderBundle.WATER.get();
        Image source = SpriteBundle.DOT_RED.get().singleImage();

        var preview = shaderSetup.createPreview(source, 10);

        assertThat(preview.frameCount()).isEqualTo(10);
    }
}
