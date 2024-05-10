package io.github.srcimon.screwbox.core.graphics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteBatchTest {

    @Test
    void entriesInOrder_twoEntriesInWrongOrder_returnsEntriesInCorrectOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 2);
        spriteBatch.add(SpriteBundle.SLIME_MOVING.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.drawOrder() == 1);
    }

    @Test
    void isEmpty_noEntries_isTrue() {
        assertThat(new SpriteBatch().isEmpty()).isTrue();
    }

    @Test
    void isEmpty_oneEntry_isFalse() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 2);

        assertThat(spriteBatch.isEmpty()).isFalse();
    }
}
