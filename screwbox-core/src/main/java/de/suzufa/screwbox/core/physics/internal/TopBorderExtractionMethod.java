package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;

public class TopBorderExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(Bounds bounds) {
        return List.of(Segment.between(bounds.origin(), bounds.topRight()));
    }

}
