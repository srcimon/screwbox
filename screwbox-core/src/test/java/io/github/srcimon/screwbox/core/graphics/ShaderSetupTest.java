package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.shader.CombinedShader;
import io.github.srcimon.screwbox.core.graphics.shader.OutlineShader;
import io.github.srcimon.screwbox.core.graphics.shader.SizeIncreaseShader;
import org.junit.jupiter.api.Test;

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
        assertThat(setup.shader().cacheKey()).isEqualTo("combined-shader-size-expansion-2-outline-#ff0000");
    }

    @Test
    void createPreview_nonAnimatedPreview_hasOneFrame() {
        ShaderSetup shaderSetup = ShaderBundle.OUTLINE_BLACK.get();
        Frame source = SpriteBundle.DOT_RED.get().singleFrame();

        var preview  = shaderSetup.createPreview(source, 2);

        assertThat(preview.frameCount()).isOne();
    }

    @Test
    void createPreview_animatedPreviewWithTenFrames_hasTenFrame() {
        ShaderSetup shaderSetup = ShaderBundle.WATER.get();
        Frame source = SpriteBundle.DOT_RED.get().singleFrame();

        var preview  = shaderSetup.createPreview(source, 10);

        assertThat(preview.frameCount()).isEqualTo(10);
    }
}
