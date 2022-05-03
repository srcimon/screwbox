package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowCircle(Offset offset, int diameter, Color color) {

    public static WindowCircle circle(final Offset offset, final int diameter) {
        return circle(offset, diameter, Color.WHITE);
    }

    public static WindowCircle circle(final Offset offset, final int diameter, final Color color) {
        return new WindowCircle(offset, diameter, color);
    }
}
