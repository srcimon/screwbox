package de.suzufa.screwbox.core.graphics.world;

import java.util.List;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldPolygon(List<Vector> points, Color color) {

    public static WorldPolygon polygon(final List<Vector> points, final Color color) {
        return new WorldPolygon(points, color);
    }
}
