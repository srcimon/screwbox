package dev.screwbox.core.graphics;

import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpriteBatchTest {

    private static final Sprite FIRST_SPRITE = SpriteBundle.DOT_YELLOW.get();
    private static final Sprite SECOND_SPRITE = SpriteBundle.SLIME_MOVING.get();

    @Test
    void entriesInOrder_twoEntriesInWrongOrder_returnsEntriesInCorrectOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(FIRST_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 2);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.drawOrder() == 1);
    }

    @Test
    void entriesInOrder_twoEntriesSameOrderNotOrthographic_returnsEntriesInSameOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();

        spriteBatch.add(FIRST_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 1);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.sprite().equals(FIRST_SPRITE));
    }

    @Test
    void entriesInOrder_twoEntriesSameOrderButFirstUsesOrthographic_returnsEntriesInCorrectOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();

        spriteBatch.add(FIRST_SPRITE, Offset.at(0, 10), SpriteDrawOptions.originalSize().sortOrthographic(), 1);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.sprite().equals(SECOND_SPRITE));
    }

    @Test
    void entriesInOrder_twoEntriesSameOrderButSecondUsesOrthographic_returnsEntriesInCorrectOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();

        spriteBatch.add(FIRST_SPRITE, Offset.at(0, 10), SpriteDrawOptions.originalSize(), 1);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize().sortOrthographic(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.sprite().equals(SECOND_SPRITE));
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
