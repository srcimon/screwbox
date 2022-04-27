package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowText(Offset offset, String text, Font font, Color color, Percentage opacity, boolean centered) {

    public static WindowText text(final Offset offset, final String text, final Font font) {
        return text(offset, text, font, Color.WHITE);
    }

    public static WindowText text(final Offset offset, final String text, final Font font, final Color color) {
        return text(offset, text, font, color, Percentage.max());
    }

    public static WindowText text(final Offset offset, final String text, final Font font, final Color color,
            final Percentage opacity) {
        return new WindowText(offset, text, font, color, opacity, false);
    }

    public static WindowText textCentered(final Offset offset, final String text, final Font font) {
        return textCentered(offset, text, font, Color.WHITE, Percentage.max());
    }

    public static WindowText textCentered(final Offset offset, final String text, final Font font, final Color color) {
        return textCentered(offset, text, font, color, Percentage.max());
    }

    public static WindowText textCentered(final Offset offset, final String text, final Font font, final Color color,
            final Percentage opacity) {
        return new WindowText(offset, text, font, color, opacity, true);
    }
}
