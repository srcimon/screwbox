package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public final class Pixelperfect {

    private Pixelperfect() {
    }

    public static double value(final double value) {
        return floor(value * 16.0) / 16.0;
    }

    public static Vector vector(final Vector vector) {
        return Vector.of(value(vector.x()), value(vector.y()));
    }

    public static Bounds bounds(final Bounds bounds) {
        final double x = floor(bounds.origin().x() / 2) * 2;
        final double y = floor(bounds.origin().y() / 2) * 2;
        return Bounds.atOrigin(x, y,
                ceil((bounds.width() + bounds.origin().x() - x) / 2) * 2,
                ceil((bounds.height() + bounds.origin().y() - y) / 2) * 2);
    }

}
