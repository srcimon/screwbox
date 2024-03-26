package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;

@Deprecated
//TODO inline in renderer
public final class AwtMapper {

    private AwtMapper() {
    }

    public static Color toColor(final java.awt.Color color) {
        final Percent opacity = Percent.of(color.getAlpha() / 255.0);
        return Color.rgb(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), (int) (color.opacity().value() * 255.0));
    }
}
