package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Segment;

import java.util.List;
import java.util.function.Function;

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

    Borders(final Function<Bounds, List<Segment>> extractSegmentsMethod) {
        this.extractSegmentsMethod = extractSegmentsMethod;
    }

    public Function<Bounds, List<Segment>> extractSegmentsMethod() {
        return extractSegmentsMethod;
    }

}
