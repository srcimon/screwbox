package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldLine(Vector from, Vector to, Color color) {

    public static WorldLine line(final Vector from, final Vector to) {
        return line(from, to, Color.WHITE);
    }

    public static WorldLine line(final Vector from, final Vector to, final Color color) {
        return new WorldLine(from, to, color);
    }
}
