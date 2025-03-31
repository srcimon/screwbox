package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.options.SpriteDrawOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for multiple sorted {@link Sprite sprites}.
 *
 * @see Canvas#drawSpriteBatch(SpriteBatch)
 */
public class SpriteBatch {

    /**
     * A single entry in a {@link SpriteBatch}.
     */
    public record SpriteBatchEntry(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry other) {
            final var order = Integer.compare(drawOrder, other.drawOrder);
            if (order != 0) {
                return order;
            }

            return options.isSortOrthographic() || other.options.isSortOrthographic()
                    ? Double.compare(maxY(), other.maxY())
                    : order;
        }

        private double maxY() {
            return options.scale() * sprite.height() + offset.y();
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
     *
     * @see SpriteDrawOptions#sortOrthographic()
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
}
