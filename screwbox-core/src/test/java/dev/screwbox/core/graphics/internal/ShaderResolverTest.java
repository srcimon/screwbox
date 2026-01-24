package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.shader.ColorizeShader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShaderResolverTest {

    @Test
    void resolveShader_overlayShaderHasProgressAndCustomShader_resultHasProgress() {
        var overlayShader = ShaderSetup.shader(new ColorizeShader(Color.ORANGE)).progress(Percent.max());
        var customShader = ShaderSetup.shader(new ColorizeShader(Color.ORANGE));

        var resolvedShader = ShaderResolver.resolveShader(overlayShader, customShader);
        assertThat(resolvedShader.progress()).isEqualTo(Percent.max());
    }
}
