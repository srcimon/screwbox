package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class TopBorderExtractionMethod implements ExtractSegmentsMethod {

    @Override
    public List<Segment> segments(Bounds bounds) {
        Vector upperRight = Vector.of(bounds.maxX(), bounds.minY());

        return List.of(Segment.between(bounds.origin(), upperRight));
    }

}
