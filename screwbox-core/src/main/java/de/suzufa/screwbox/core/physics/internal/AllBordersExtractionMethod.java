package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class AllBordersExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(final Bounds bounds) {
        final Vector topRight = bounds.topRight();
        final Vector bottomRight = bounds.bottomRight();
        final Vector bottomLeft = bounds.bottomLeft();

        return List.of(
                Segment.between(bounds.origin(), topRight),
                Segment.between(topRight, bottomRight),
                Segment.between(bottomRight, bottomLeft),
                Segment.between(bottomLeft, bounds.origin()));
    }

}
