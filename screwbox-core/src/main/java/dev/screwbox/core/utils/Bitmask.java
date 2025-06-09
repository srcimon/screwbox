package dev.screwbox.core.utils;

import dev.screwbox.core.graphics.Offset;

//TODO move to own package
public class Bitmask {

    enum Locations {
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1),
        NORTH(0, -1);

        private final Offset offset;

        public Offset offset() {
            return offset;
        }

        Locations(final int x, final int y) {
            offset = Offset.at(x, y);
        }
    }

    private boolean[] mask = new boolean[8];

    public void setSame(final Locations location, final boolean isSame) {
        mask[location.ordinal()] = isSame;
    }

}
