package de.suzufa.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Vector;

public class SpriteBatch {

    private List<SpriteBatchEntry> entries = new ArrayList<>();

    public final record SpriteBatchEntry(
            Sprite sprite, Vector position, double scale, Percentage opacity, Angle rotation, FlipMode flipMode,
            int drawOrder)
            implements Comparable<SpriteBatchEntry> {

        @Override
        public int compareTo(final SpriteBatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }
    }

    public void addEntry(final Sprite sprite, final Vector position, final double scale, final Percentage opacity,
            final Angle rotation,
            final FlipMode flipMode,
            final int drawOrder) {
        this.entries.add(new SpriteBatchEntry(sprite, position, scale, opacity, rotation, flipMode, drawOrder));
    }

    public List<SpriteBatchEntry> entriesInDrawOrder() {
        Collections.sort(entries);
        return entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
