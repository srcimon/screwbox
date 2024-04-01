package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

//TODO Javadoc
public record SpriteFillOptions(Offset offset, double scale, Percent opacity) {

    public static SpriteFillOptions scaled(final double scale) {
        return new SpriteFillOptions(Offset.origin(), scale, Percent.max());
    }
}
