package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class AllBordersExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(Bounds bounds) {
        Vector upperRight = Vector.of(bounds.maxX(), bounds.minY());
        Vector lowerRight = Vector.of(bounds.maxX(), bounds.maxY());
        Vector lowerLeft = Vector.of(bounds.minX(), bounds.maxY());

        return List.of(
                Segment.between(bounds.origin(), upperRight),
                Segment.between(upperRight, lowerRight),
                Segment.between(lowerRight, lowerLeft),
                Segment.between(lowerLeft, bounds.origin()));
    }

}
