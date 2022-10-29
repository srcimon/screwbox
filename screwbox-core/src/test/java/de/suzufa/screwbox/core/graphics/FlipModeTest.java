package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FlipModeTest {

    @Test
    void isVertical_returnsIfIsFlippedVertical() {
        assertThat(FlipMode.VERTICAL.isVertical()).isTrue();
        assertThat(FlipMode.BOTH.isVertical()).isTrue();

        assertThat(FlipMode.HORIZONTAL.isVertical()).isFalse();
        assertThat(FlipMode.NONE.isVertical()).isFalse();
    }

    @Test
    void isVorizontal_returnsIfIsFlippedHorizontal() {
        assertThat(FlipMode.HORIZONTAL.isHorizontal()).isTrue();
        assertThat(FlipMode.BOTH.isHorizontal()).isTrue();

        assertThat(FlipMode.VERTICAL.isHorizontal()).isFalse();
        assertThat(FlipMode.NONE.isHorizontal()).isFalse();
    }

    @Test
    void invertVertical_invertsVerticalComponent() {
        assertThat(FlipMode.NONE.invertVertical()).isEqualTo(FlipMode.VERTICAL);
        assertThat(FlipMode.HORIZONTAL.invertVertical()).isEqualTo(FlipMode.BOTH);
        assertThat(FlipMode.VERTICAL.invertVertical()).isEqualTo(FlipMode.NONE);
        assertThat(FlipMode.BOTH.invertVertical()).isEqualTo(FlipMode.HORIZONTAL);
    }
}
