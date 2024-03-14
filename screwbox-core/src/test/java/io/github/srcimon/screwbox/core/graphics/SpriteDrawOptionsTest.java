package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteDrawOptionsTest {

    @Test
    void scaled_returnsScaledInstance() {
        var options = SpriteDrawOptions.scaled(2);

        assertThat(options.scale()).isEqualTo(2);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.none());
        assertThat(options.flip()).isEqualTo(SpriteDrawOptions.Flip.NONE);
    }

    @Test
    void originalSize_returnsInstanceWithoutScale() {
        var options = SpriteDrawOptions.originalSize()
                .flip(SpriteDrawOptions.Flip.VERTICAL)
                .rotation(Rotation.degrees(30));

        assertThat(options.scale()).isEqualTo(1);
        assertThat(options.opacity()).isEqualTo(Percent.max());
        assertThat(options.rotation()).isEqualTo(Rotation.degrees(30));
        assertThat(options.flip()).isEqualTo(SpriteDrawOptions.Flip.VERTICAL);
    }

    @Test
    void scale_changesScale() {
        var options = SpriteDrawOptions.originalSize().scale(4);

        assertThat(options.scale()).isEqualTo(4);
    }

    @Test
    void isVertical_returnsIfIsFlippedVertical() {
        assertThat(SpriteDrawOptions.Flip.VERTICAL.isVertical()).isTrue();
        assertThat(SpriteDrawOptions.Flip.BOTH.isVertical()).isTrue();

        assertThat(SpriteDrawOptions.Flip.HORIZONTAL.isVertical()).isFalse();
        assertThat(SpriteDrawOptions.Flip.NONE.isVertical()).isFalse();
    }

    @Test
    void isVorizontal_returnsIfIsFlippedHorizontal() {
        assertThat(SpriteDrawOptions.Flip.HORIZONTAL.isHorizontal()).isTrue();
        assertThat(SpriteDrawOptions.Flip.BOTH.isHorizontal()).isTrue();

        assertThat(SpriteDrawOptions.Flip.VERTICAL.isHorizontal()).isFalse();
        assertThat(SpriteDrawOptions.Flip.NONE.isHorizontal()).isFalse();
    }

    @Test
    void invertVertical_invertsVerticalComponent() {
        assertThat(SpriteDrawOptions.Flip.NONE.invertVertical()).isEqualTo(SpriteDrawOptions.Flip.VERTICAL);
        assertThat(SpriteDrawOptions.Flip.HORIZONTAL.invertVertical()).isEqualTo(SpriteDrawOptions.Flip.BOTH);
        assertThat(SpriteDrawOptions.Flip.VERTICAL.invertVertical()).isEqualTo(SpriteDrawOptions.Flip.NONE);
        assertThat(SpriteDrawOptions.Flip.BOTH.invertVertical()).isEqualTo(SpriteDrawOptions.Flip.HORIZONTAL);
    }
}
