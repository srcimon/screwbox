package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;

import java.awt.*;

public final class AwtMapper {

    private AwtMapper() {
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), color.opacity().rangeValue(0, 255));
    }

    public static Font toAwtFont(final SystemTextDrawOptions options) {
        final int value = options.isBold() ? Font.BOLD : Font.ROMAN_BASELINE;
        final int realValue = options.isItalic() ? value + Font.ITALIC : value;
        return new Font(options.fontName(), realValue, options.size());
    }
}
