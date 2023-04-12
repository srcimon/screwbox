package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;

import java.util.Map;

public final class AwtMapper {

    private static final Map<Font.Style, Integer> STYLES = Map.of(
            Font.Style.NORMAL, 0,
            Font.Style.BOLD, 1,
            Font.Style.ITALIC, 2,
            Font.Style.ITALIC_BOLD, 3);

    private AwtMapper() {
    }

    public static Color toColor(final java.awt.Color color) {
        final Percent opacity = Percent.of(color.getAlpha() / 255.0);
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
