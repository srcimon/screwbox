package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;

public final class AwtMapper {

    private AwtMapper() {
    }

    //TODO I dont understand that workaround!
    public static java.awt.Color toAwtColor(final Color color) {
        return new java.awt.Color(color.r(), color.g(), color.b(), color.opacity().rangeValue(0, 254));
    }
}
