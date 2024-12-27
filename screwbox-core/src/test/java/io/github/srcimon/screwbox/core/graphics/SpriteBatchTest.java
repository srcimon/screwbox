package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
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

        spriteBatch.add(FIRST_SPRITE, Offset.at(0, 10), SpriteDrawOptions.originalSize().useOrhographicSorting(), 1);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize(), 1);

        assertThat(spriteBatch.entriesInOrder()).hasSize(2);
        assertThat(spriteBatch.entriesInOrder().getFirst()).matches(entry -> entry.sprite().equals(SECOND_SPRITE));
    }

    @Test
    void entriesInOrder_twoEntriesSameOrderButSecondUsesOrthographic_returnsEntriesInCorrectOrder() {
        SpriteBatch spriteBatch = new SpriteBatch();

        spriteBatch.add(FIRST_SPRITE, Offset.at(0, 10), SpriteDrawOptions.originalSize(), 1);
        spriteBatch.add(SECOND_SPRITE, Offset.origin(), SpriteDrawOptions.originalSize().useOrhographicSorting(), 1);

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

    @Test
    void translate_batchHasEntries_translatesEntries() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 2);
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.at(10, 4), SpriteDrawOptions.originalSize(), 2);

        var translated = spriteBatch.translate(Offset.at(10, 2));

        assertThat(translated.entriesInOrder().getFirst().offset()).isEqualTo(Offset.at(10, 2));
        assertThat(translated.entriesInOrder().getLast().offset()).isEqualTo(Offset.at(20, 6));
    }

    @Test
    void translate_batchHasNoEntries_isUnchanged() {
        SpriteBatch spriteBatch = new SpriteBatch();

        var translated = spriteBatch.translate(Offset.at(10, 2));

        assertThat(translated).isEqualTo(spriteBatch);
    }

    @Test
    void translate_offsetIsOrigin_isUnchanged() {
        SpriteBatch spriteBatch = new SpriteBatch();
        spriteBatch.add(SpriteBundle.DOT_YELLOW.get(), Offset.origin(), SpriteDrawOptions.originalSize(), 2);

        var translated = spriteBatch.translate(Offset.at(0,0));

        assertThat(translated).isEqualTo(spriteBatch);
    }
}
