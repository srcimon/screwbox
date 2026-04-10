package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;

import java.util.List;
import java.util.function.Function;

public enum Borders {

    ALL(Bounds::borders),
    TOP(bounds -> List.of(
        Line.between(bounds.origin(), bounds.topRight()))),
    VERTICAL(bounds -> List.of(
        Line.between(bounds.topRight(), bounds.bottomRight()),
        Line.between(bounds.bottomLeft(), bounds.origin()))),
    HORIZONTAL(bounds -> List.of(
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
