package de.suzufa.screwbox.core.graphics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FlipTest {

    @Test
    void isVertical_returnsIfIsFlippedVertical() {
        assertThat(Flip.VERTICAL.isVertical()).isTrue();
        assertThat(Flip.BOTH.isVertical()).isTrue();

        assertThat(Flip.HORIZONTAL.isVertical()).isFalse();
        assertThat(Flip.NONE.isVertical()).isFalse();
    }

    @Test
    void isVorizontal_returnsIfIsFlippedHorizontal() {
        assertThat(Flip.HORIZONTAL.isHorizontal()).isTrue();
        assertThat(Flip.BOTH.isHorizontal()).isTrue();

        assertThat(Flip.VERTICAL.isHorizontal()).isFalse();
        assertThat(Flip.NONE.isHorizontal()).isFalse();
    }

    @Test
    void invertVertical_invertsVerticalComponent() {
        assertThat(Flip.NONE.invertVertical()).isEqualTo(Flip.VERTICAL);
        assertThat(Flip.HORIZONTAL.invertVertical()).isEqualTo(Flip.BOTH);
        assertThat(Flip.VERTICAL.invertVertical()).isEqualTo(Flip.NONE);
        assertThat(Flip.BOTH.invertVertical()).isEqualTo(Flip.HORIZONTAL);
    }
}
