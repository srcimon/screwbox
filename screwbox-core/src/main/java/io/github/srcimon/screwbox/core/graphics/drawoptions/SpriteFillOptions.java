package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import static java.util.Objects.requireNonNull;

/**
 * Customize the drawing of filling the {@link Screen} with {@link Sprite}s.
 *
 * @param offset  the {@link Offset} used to fill the {@link Screen}
 * @param scale   the scale of the {@link Sprite}s used to fill the {@link Screen}
 * @param opacity the opacity used to fill the {@link Screen}
 * @see Canvas#fillWith(Sprite, SpriteFillOptions)
 */
public record SpriteFillOptions(Offset offset, double scale, Percent opacity) {

    public SpriteFillOptions {
        if (scale <= 0) {
            throw new IllegalArgumentException("scale must be positive");
        }
        requireNonNull(offset, "offset must not be null");
        requireNonNull(opacity, "opacity must not be null");
    }

    /**
     * Creates a new instance with given {@link #scale()}.
     */
    public static SpriteFillOptions scale(final double scale) {
        return new SpriteFillOptions(Offset.origin(), scale, Percent.max());
    }

    /**
     * Creates a new instance with given {@link #offset()}
     */
    public SpriteFillOptions offset(final Offset offset) {
        return new SpriteFillOptions(offset, scale, opacity);
    }

    /**
     * Creates a new instance with given {@link #opacity()}.
     */
    public SpriteFillOptions opacity(final Percent opacity) {
        return new SpriteFillOptions(offset, scale, opacity);
    }
}
