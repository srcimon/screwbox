package de.suzufa.screwbox.core.graphics.internal;

import java.util.Map;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Font.Style;

final public class AwtMapper {

    private static final Map<Style, Integer> STYLES = Map.of(
            Style.NORMAL, 0,
            Style.BOLD, 1,
            Style.ITALIC, 2,
            Style.ITALIC_BOLD, 3);

    private AwtMapper() {
    }

    // TODO: Test
    public static Color toColor(final java.awt.Color color) {
        final Percentage opacity = Percentage.of(color.getAlpha() / 255.0);
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), (int) (color.opacity().value() * 255.0));
    }

    public static java.awt.Font toAwtFont(final Font font) {
        final int style = STYLES.get(font.style());
        return new java.awt.Font(font.name(), style, font.size());
    }

}
