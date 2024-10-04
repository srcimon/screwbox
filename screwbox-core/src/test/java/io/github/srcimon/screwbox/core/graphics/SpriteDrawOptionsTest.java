package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteDrawOptionsTest {

    @Test
    void scaled_returnsScaledInstance() {
        var options = SpriteDrawOptions.scaled(2).invertVerticalFlip().spin(Percent.of(0.4));

        assertThat(options.scale()).isEqualTo(2);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.none());
        assertThat(options.isFlipHorizontal()).isFalse();
        assertThat(options.isFlipVertical()).isTrue();
        assertThat(options.spin()).isEqualTo(Percent.of(0.4));
    }

    @Test
    void originalSize_returnsInstanceWithoutScale() {
        var options = SpriteDrawOptions.originalSize()
                .flipHorizontal(true)
                .flipVertical(true)
                .rotation(Rotation.degrees(30))
                .spinHorizontal(false);

        assertThat(options.scale()).isEqualTo(1);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.degrees(30));
        assertThat(options.isFlipHorizontal()).isTrue();
        assertThat(options.isFlipVertical()).isTrue();
        assertThat(options.isSpinHorizontal()).isFalse();
    }

    @Test
    void scaleA_changesScale() {
        var options = SpriteDrawOptions.originalSize().scale(4);

        assertThat(options.scale()).isEqualTo(4);
    }
}
