package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for multiple sorted {@link Sprite sprites}.
 * 
 * @see Screen#drawSpriteBatch(SpriteBatch)
 */
public class SpriteBatch {

    /**
     * A single entry in a {@link SpriteBatch}.
     */
    public record SpriteBatchEntry(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry other) {
            return Integer.compare(drawOrder, other.drawOrder);
        }
    }

    private final List<SpriteBatchEntry> entries = new ArrayList<>();

    /**
     * Adds a {@link Sprite} to the batch.
     */
    public void add(final Sprite sprite, final Offset offset, final SpriteDrawOptions options, final int drawOrder) {
        entries.add(new SpriteBatchEntry(sprite, offset, options, drawOrder));
    }

    /**
     * Returns all {@link SpriteBatchEntry entries} in order.
     */
    public List<SpriteBatchEntry> entriesInOrder() {
        Collections.sort(entries);
        return entries;
    }

    /**
     * Returns {@code true} if batch has no entries.
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public List<SpriteBatchEntry> entries() {
        return entries;
    }
}
