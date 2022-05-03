package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowLine(Offset from, Offset to, Color color) {

    public static WindowLine line(final Offset from, final Offset to) {
        return line(from, to, Color.WHITE);
    }

    public static WindowLine line(final Offset from, final Offset to, final Color color) {
        return new WindowLine(from, to, color);
    }
}
