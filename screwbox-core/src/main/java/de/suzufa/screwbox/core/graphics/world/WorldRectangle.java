package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldRectangle(Bounds bounds, Color color, Percentage opacity) {

    public static WorldRectangle rectangle(final Bounds bounds, final Color color) {
        return rectangle(bounds, color, Percentage.max());
    }

    public static WorldRectangle rectangle(final Bounds bounds, final Color color, final Percentage opacity) {
        return new WorldRectangle(bounds, color, opacity);
    }
}
