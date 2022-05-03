package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public record WindowRectangle(WindowBounds bounds, Color color) {

    public static WindowRectangle dot(final Offset offset) {
        return dot(offset, Color.WHITE);
    }

    public static WindowRectangle dot(final Offset offset, final Color color) {
        return rectangle(offset, Dimension.of(1, 1), color);
    }

    public static WindowRectangle rectangle(final Offset offset, final Dimension dimension, final Color color) {
        final WindowBounds bounds = new WindowBounds(offset, dimension);
        return rectangle(bounds, color);
    }

    public static WindowRectangle rectangle(final WindowBounds bounds, final Color color) {
        return new WindowRectangle(bounds, color);
    }

}
