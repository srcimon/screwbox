package io.github.simonbas.screwbox.core.graphics;

import io.github.simonbas.screwbox.core.Angle;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteBatch {

    private List<SpriteBatchEntry> entries = new ArrayList<>();

    public final record SpriteBatchEntry(
            Sprite sprite, Vector position, double scale, Percent opacity, Angle rotation, Flip flip,
            int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }
    }

    public void addEntry(final Sprite sprite, final Vector position, final double scale, final Percent opacity,
            final Angle rotation,
            final Flip flip,
            final int drawOrder) {
        this.entries.add(new SpriteBatchEntry(sprite, position, scale, opacity, rotation, flip, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInDrawOrder() {
        Collections.sort(entries);
        return entries;
    }

}
