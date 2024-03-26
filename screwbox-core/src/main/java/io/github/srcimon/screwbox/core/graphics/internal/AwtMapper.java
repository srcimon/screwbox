package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Color;

public final class AwtMapper {

    private AwtMapper() {
    }

    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), (int) (color.opacity().value() * 255.0));
    }
}
