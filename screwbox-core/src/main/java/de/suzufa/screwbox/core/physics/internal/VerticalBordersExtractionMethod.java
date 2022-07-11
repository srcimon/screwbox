package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;

public class VerticalBordersExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(final Bounds bounds) {
        return List.of(
                Segment.between(bounds.topRight(), bounds.bottomRight()),
                Segment.between(bounds.bottomLeft(), bounds.origin()));
    }
}
