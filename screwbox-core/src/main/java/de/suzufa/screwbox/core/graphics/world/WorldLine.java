package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldLine(Vector from, Vector to, Color color, Percentage opacity) {

    public static WorldLine line(final Vector from, final Vector to) {
        return line(from, to, Percentage.max());
    }

    public static WorldLine line(final Vector from, final Vector to, final Percentage opacity) {
        return line(from, to, Color.WHITE, opacity);
    }

    public static WorldLine line(final Vector from, final Vector to, final Color color) {
        return line(from, to, color, Percentage.max());
    }

    public static WorldLine line(final Vector from, final Vector to, final Color color, final Percentage opacity) {
        return new WorldLine(from, to, color, opacity);
    }
}
