package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatNoException;

class ShaderBundleTest {

    @ParameterizedTest
    @EnumSource(ShaderBundle.class)
    void verifyAllShadersAreValid(ShaderBundle shader) {
        assertThatNoException().isThrownBy(shader::get);
        shader.get().createPreview(SpriteBundle.BOX_STRIPED.get().addBorder(2, Color.TRANSPARENT).singleImage(), SpriteBundle.SHADER_PREVIEW.get().singleImage(), 20).scaled(2).exportGif(shader.name());
    }

}
