package dev.screwbox.core.utils;

//TODO move to own package
public class Bitmask {

    enum Locations {
        NORTH(0, -1),
        NORTH_EAST(1, -1),
        EAST(1, 0),
        SOUTH_EAST(1, 1),
        SOUTH(0, 1),
        SOUTH_WEST(-1, 1),
        WEST(-1, 0),
        NORTH_WEST(-1, -1);

        private final int x;
        private final int y;

        Locations(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }
}
