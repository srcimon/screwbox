package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteBatch {

    private final List<SpriteBatchEntry> entries = new ArrayList<>();

    public record SpriteBatchEntry(
            Sprite sprite, Vector position, double scale, Percent opacity, Rotation rotation, Flip flip,
            int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }
    }

    public void addEntry(final Sprite sprite, final Vector position, final double scale, final Percent opacity,
            final Rotation rotation,
            final Flip flip,
            final int drawOrder) {
        this.entries.add(new SpriteBatchEntry(sprite, position, scale, opacity, rotation, flip, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInDrawOrder() {
        Collections.sort(entries);
        return entries;
    }

}
