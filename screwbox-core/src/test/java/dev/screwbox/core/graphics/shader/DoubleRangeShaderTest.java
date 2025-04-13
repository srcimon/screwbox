package dev.screwbox.core.graphics.shader;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DoubleRangeShaderTest {

    @Test
    void cacheKey_containsUniqueKey() {
        var shader = new DoubleRangeShader(5, 10, val -> new DistortionShader(10, val, val));
        assertThat(shader.cacheKey()).isEqualTo("IntRangeShader-5.0-10.0-DistortionShader-10-5.0-5.0");
    }
}
