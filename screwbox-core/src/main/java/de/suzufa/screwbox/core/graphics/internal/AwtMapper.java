package de.suzufa.screwbox.core.graphics.internal;

import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;

public final class AwtMapper {

    private AwtMapper() {
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), (int) (color.opacity().value() * 255.0));
    }

    public static java.awt.Font toAwtFont(final Font font) {
        return new java.awt.Font(font.name(), getAwtStyle(font.style()), font.size());
    }

    private static int getAwtStyle(final Style style) {
        switch (style) {
        case NORMAL:
            return 0;
        case BOLD:
            return 1;
        default:
            return 2;
        }
    }

}
