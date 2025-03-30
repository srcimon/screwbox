package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.graphics.Color;

public record PolygonDrawOptions(Color fillColor) {

    public static PolygonDrawOptions filled(Color color) {
        return new PolygonDrawOptions(color);
    }
}
