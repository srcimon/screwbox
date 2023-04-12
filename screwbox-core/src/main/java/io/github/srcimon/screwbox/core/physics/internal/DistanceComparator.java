package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.Vector;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Vector> {

    private final Vector origin;

    public DistanceComparator(final Vector origin) {
        this.origin = origin;
    }

    @Override
    public int compare(final Vector vector, final Vector other) {
        return Double.compare(
                vector.distanceTo(origin),
                other.distanceTo(origin));
    }

}
