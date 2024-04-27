package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Vector;

public final class Pixelperfect {

    private Pixelperfect() {
    }

    public static double value(final double value) {
        return Math.floor(value * 16.0) / 16.0;
    }

    public static Vector vector(final Vector vector) {
        return Vector.of(value(vector.x()), value(vector.y()));
    }


}
