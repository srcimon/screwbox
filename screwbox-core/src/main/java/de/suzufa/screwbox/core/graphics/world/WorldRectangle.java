package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldRectangle(Bounds bounds, Color color) {

    public static WorldRectangle rectangle(final Bounds bounds, final Color color) {
        return new WorldRectangle(bounds, color);
    }

}
