package io.github.srcimon.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteBatch {

    public record SpriteBatchEntry(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry other) {
            return Integer.compare(drawOrder, other.drawOrder);
        }
    }

    private final List<SpriteBatchEntry> entries = new ArrayList<>();

    public void add(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder) {
        entries.add(new SpriteBatchEntry(sprite, offset, options, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInOrder() {
        Collections.sort(entries);
        return entries;
    }
}
