package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.graphics.ShaderBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteDrawOptionsTest {

    @Test
    void scaled_returnsScaledInstance() {
        var options = SpriteDrawOptions.scaled(2).invertVerticalFlip().spin(Percent.of(0.4)).sortOrthographic().ignoreOverlayShader();

        assertThat(options.scale()).isEqualTo(2);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.none());
        assertThat(options.isFlipHorizontal()).isFalse();
        assertThat(options.isFlipVertical()).isTrue();
        assertThat(options.isSortOrthographic()).isTrue();
        assertThat(options.spin()).isEqualTo(Percent.of(0.4));
        assertThat(options.isIgnoreOverlayShader()).isTrue();
    }

    @Test
    void originalSize_returnsInstanceWithoutScale() {
        var options = SpriteDrawOptions.originalSize()
                .flipHorizontal(true)
                .flipVertical(true)
                .rotation(Rotation.degrees(30))
                .shaderSetup(ShaderBundle.BREEZE)
                .spinHorizontal(false);

        assertThat(options.isSortOrthographic()).isFalse();
        assertThat(options.scale()).isEqualTo(1);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.degrees(30));
        assertThat(options.isFlipHorizontal()).isTrue();
        assertThat(options.isFlipVertical()).isTrue();
        assertThat(options.shaderSetup()).isEqualTo(ShaderBundle.BREEZE.get());
        assertThat(options.isSpinHorizontal()).isFalse();
    }

    @Test
    void scaleA_changesScale() {
        var options = SpriteDrawOptions.originalSize().scale(4);

        assertThat(options.scale()).isEqualTo(4);
    }
}
