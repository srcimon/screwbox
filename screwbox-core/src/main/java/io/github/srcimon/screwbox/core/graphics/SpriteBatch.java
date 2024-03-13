package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO Javadoc
public class SpriteBatch {

    private final List<SpriteBatchEntry> entries = new ArrayList<>();

    public record SpriteBatchEntry(Sprite sprite, Vector position, SpriteDrawOptions options, int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }
    }

    public void addEntry(final Sprite sprite, final Vector position, SpriteDrawOptions options, final int drawOrder) {
        this.entries.add(new SpriteBatchEntry(sprite, position, options, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInDrawOrder() {
        Collections.sort(entries);
        return entries;
    }

}
