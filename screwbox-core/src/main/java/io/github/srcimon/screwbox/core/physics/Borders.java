package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;

import java.util.List;
import java.util.function.Function;

public enum Borders {

    ALL(bounds -> List.of(
            Line.between(bounds.origin(), bounds.topRight()),
            Line.between(bounds.topRight(), bounds.bottomRight()),
            Line.between(bounds.bottomRight(), bounds.bottomLeft()),
            Line.between(bounds.bottomLeft(), bounds.origin()))),
    TOP_ONLY(bounds -> List.of(Line.between(bounds.origin(), bounds.topRight()))),
    VERTICAL_ONLY(bounds -> List.of(
            Line.between(bounds.topRight(), bounds.bottomRight()),
            Line.between(bounds.bottomLeft(), bounds.origin())));

    private final Function<Bounds, List<Line>> extractSegmentsMethod;

    Borders(final Function<Bounds, List<Line>> extractSegmentsMethod) {
        this.extractSegmentsMethod = extractSegmentsMethod;
    }

    public List<Line> extractSegments(Bounds bounds) {
        return extractSegmentsMethod.apply(bounds);
    }

}
