package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;

import java.util.List;

//TODO move to own package
public class Bitmask {

    public enum Neighbour {
        NORTH(0, -1),
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1);

        private final Offset offset;

        public Offset offset() {
            return offset;
        }

        Neighbour(final int x, final int y) {
            offset = Offset.at(x, y);
        }
    }

    private final int index;

    public Bitmask(final List<Neighbour> identicalNeighbors) {
        final boolean[] vals = new boolean[8];
        for (final var neighbour : identicalNeighbors) {
            vals[neighbour.ordinal()] = true;
        }

        // invalidate edges to reduce index variety
        for (int i = 1; i < vals.length; i += 2) {
            int last = i -1;
            int next = i +1 > 7 ? 0 : i+1;
            if (!vals[last] || !vals[next]) {
                vals[i] = false;
            }
        }

        double calculatedIndex = 0;
        for (int i = 0; i < vals.length; i++) {
            calculatedIndex += vals[i] ? Math.pow(2,i) : 0;
        }
        index = (int) calculatedIndex;
    }

    public int index() {
        return index;
    }

}
