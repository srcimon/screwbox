package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.ShaderBundle;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteDrawOptionsTest {

    @Test
    void scaled_returnsScaledInstance() {
        var options = SpriteDrawOptions.scaled(2)
                .invertVerticalFlip()
                .spin(Percent.of(0.4))
                .zIndex(12)
                .ignoreOverlayShader();

        assertThat(options.scale()).isEqualTo(2);
        assertThat(options.zIndex()).isEqualTo(12);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Angle.none());
        assertThat(options.isFlippedHorizontal()).isFalse();
        assertThat(options.isFlippedVertical()).isTrue();
        assertThat(options.spin()).isEqualTo(Percent.of(0.4));
        assertThat(options.isIgnoreOverlayShader()).isTrue();
    }

    @Test
    void originalSize_returnsInstanceWithoutScale() {
        var options = SpriteDrawOptions.originalSize()
                .flippedHorizontal(true)
                .flippedVertical(true)
                .rotation(Angle.degrees(30))
                .shaderSetup(ShaderBundle.BREEZE)
                .spinHorizontal(false);

        assertThat(options.scale()).isEqualTo(1);
        assertThat(options.zIndex()).isEqualTo(Integer.MIN_VALUE);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Angle.degrees(30));
        assertThat(options.isFlippedHorizontal()).isTrue();
        assertThat(options.isFlippedVertical()).isTrue();
        assertThat(options.shaderSetup()).isEqualTo(ShaderBundle.BREEZE.get());
        assertThat(options.isSpinHorizontal()).isFalse();
    }

    @Test
    void scaleA_changesScale() {
        var options = SpriteDrawOptions.originalSize().scale(4);

        assertThat(options.scale()).isEqualTo(4);
    }

    @Test
    void invertVerticalFlip_notFlipped_isVerticallyFlipped() {
        var options = SpriteDrawOptions.originalSize()
                .scale(4)
                .invertVerticalFlip();

        assertThat(options.isFlippedVertical()).isTrue();
    }

    @Test
    void invertVerticalFlip_isFlipped_isNotVerticallyFlipped() {
        var options = SpriteDrawOptions.originalSize()
                .scale(4)
                .flippedVertical(true)
                .invertVerticalFlip();

        assertThat(options.isFlippedVertical()).isFalse();
    }
}
