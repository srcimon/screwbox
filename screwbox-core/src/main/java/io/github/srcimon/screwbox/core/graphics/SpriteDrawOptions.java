package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO rectangle immutable
//TODO line immutable
//TODO circle immutable
//TODO: camerashakeoptions immutable

/**
 * Customize the drawing of {@link Sprite}s.
 * @param scale the scale of the {@link Sprite}
 * @param opacity the opacity of the {@link Sprite}
 * @param rotation the {@link Rotation} of the {@link Sprite}
 * @param flip the {@link Flip} of the {@link Sprite}
 *             
 * @see Screen#drawSprite(Sprite, Offset, SpriteDrawOptions)
 */
public record SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, Flip flip) {

    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), Flip.NONE);
    }

    /**
     * Creates a new instance with {@link #scale()} 1.
     */
    public static SpriteDrawOptions originalSize() {
        return scaled(1);
    }

    /**
     * Creates a new instance with given {@link #scale()}.
     */
    public static SpriteDrawOptions scaled(double scale) {
        return new SpriteDrawOptions(scale);
    }

    /**
     * Creates a new instance with updated {@link #scale()}.
     */
    public SpriteDrawOptions scale(final double scale) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link #rotation()}.
     */
    public SpriteDrawOptions rotation(final Rotation rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link Flip}.
     */
    public SpriteDrawOptions flip(final Flip flip) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

}
