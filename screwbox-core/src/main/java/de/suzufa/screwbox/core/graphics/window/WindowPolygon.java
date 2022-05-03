package de.suzufa.screwbox.core.graphics.window;

import java.util.List;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowPolygon(List<Offset> points, Color color) {

    public static WindowPolygon polygon(final List<Offset> points, final Color color) {
        return new WindowPolygon(points, color);
    }
}
