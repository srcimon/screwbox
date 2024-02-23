package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;

import java.util.List;
import java.util.function.Function;

//TODO also accessed from graphics. make public or move to common internal package?
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

    private final Function<Bounds, List<Line>> extractionMethod;

    Borders(final Function<Bounds, List<Line>> extractionMethod) {
        this.extractionMethod = extractionMethod;
    }

    public List<Line> extractFrom(final Bounds bounds) {
        return extractionMethod.apply(bounds);
    }

}
