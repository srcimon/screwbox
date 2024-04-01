package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

//TODO Javadoc
public record SpriteFillOptions(Offset offset, double scale, Percent opacity) {

    public static SpriteFillOptions scale(final double scale) {
        return new SpriteFillOptions(Offset.origin(), scale, Percent.max());
    }

    public SpriteFillOptions offset(final Offset offset) {
        return new SpriteFillOptions(offset, scale, opacity);
    }

    public SpriteFillOptions opacity(final Percent opacity) {
        return new SpriteFillOptions(offset, scale, opacity);
    }
}
