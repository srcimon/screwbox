package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class VerticalBordersExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(final Bounds bounds) {
        final Vector upperRight = Vector.of(bounds.maxX(), bounds.minY());
        final Vector lowerRight = Vector.of(bounds.maxX(), bounds.maxY());
        final Vector lowerLeft = Vector.of(bounds.minX(), bounds.maxY());

        return List.of(
                Segment.between(upperRight, lowerRight),
                Segment.between(lowerLeft, bounds.origin()));
    }
}
