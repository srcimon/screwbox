package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;

public interface ExtractSegmentsMethod {

    List<Segment> segments(Bounds bounds);
}
