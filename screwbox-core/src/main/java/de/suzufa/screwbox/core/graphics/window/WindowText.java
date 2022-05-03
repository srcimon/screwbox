package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowText(Offset offset, String text, Font font, Color color, boolean centered) {

    public static WindowText text(final Offset offset, final String text, final Font font) {
        return text(offset, text, font, Color.WHITE);
    }

    public static WindowText text(final Offset offset, final String text, final Font font, final Color color) {
        return new WindowText(offset, text, font, color, false);
    }

    public static WindowText textCentered(final Offset offset, final String text, final Font font) {
        return textCentered(offset, text, font, Color.WHITE);
    }

    public static WindowText textCentered(final Offset offset, final String text, final Font font, final Color color) {
        return new WindowText(offset, text, font, color, true);
    }
}
