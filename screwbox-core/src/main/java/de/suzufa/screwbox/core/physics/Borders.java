package de.suzufa.screwbox.core.physics;

import java.util.List;
import java.util.function.Function;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;

public enum Borders {

    ALL(bounds -> List.of(
            Segment.between(bounds.origin(), bounds.topRight()),
            Segment.between(bounds.topRight(), bounds.bottomRight()),
            Segment.between(bounds.bottomRight(), bounds.bottomLeft()),
            Segment.between(bounds.bottomLeft(), bounds.origin()))),
    TOP_ONLY(bounds -> List.of(Segment.between(bounds.origin(), bounds.topRight()))),
    VERTICAL_ONLY(bounds -> List.of(
            Segment.between(bounds.topRight(), bounds.bottomRight()),
            Segment.between(bounds.bottomLeft(), bounds.origin())));

    private final Function<Bounds, List<Segment>> extractSegmentsMethod;

    private Borders(final Function<Bounds, List<Segment>> extractSegmentsMethod) {
        this.extractSegmentsMethod = extractSegmentsMethod;
    }

    Function<Bounds, List<Segment>> extractSegmentsMethod() {
        return extractSegmentsMethod;
    }

}
