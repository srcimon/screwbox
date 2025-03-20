package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.shader.ColorizeShader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShaderResolverTest {

    @Test
    void resolveShader_overlayShaderHasProgressAndCustomShader_resultHasProgress() {
        var overlayShader = ShaderSetup.shader(new ColorizeShader(Color.ORANGE)).progress(Percent.max());
        var customShader = ShaderSetup.shader(new ColorizeShader(Color.ORANGE));

        var resolvedShader = ShaderResolver.resolveShader(overlayShader, customShader, false);
        assertThat(resolvedShader.progress()).isEqualTo(Percent.max());
    }
}
