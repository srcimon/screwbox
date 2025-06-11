package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AutoTileIndex {

    private enum Neighbour {
        NORTH(0, -1),
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1);

        private final Offset offset;

        Neighbour(final int x, final int y) {
            offset = Offset.at(x, y);
        }
    }

    private final List<Neighbour> neighbours = new ArrayList<>();

    public AutoTileIndex(Offset offset, Predicate<Offset> isNeighbor) {
        for (final var neighbour : Neighbour.values()) {
            var location = offset.add(neighbour.offset);
            if(isNeighbor.test(location)) {
                neighbours.add(neighbour);
            }
        }
    }

    public int index3x3Minimal() {
        final boolean[] vals = new boolean[8];
        for (final var neighbour : neighbours) {
            vals[neighbour.ordinal()] = true;
        }

        // invalidate edges to reduce autoTileIndex variety
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
        return  (int) calculatedIndex;
    }
}
