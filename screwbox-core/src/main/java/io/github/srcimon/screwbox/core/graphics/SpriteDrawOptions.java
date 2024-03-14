package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

import java.io.Serializable;

//TODO rectangle immutable
//TODO line immutable
//TODO circle immutable
//TODO: camerashakeoptions immutable

/**
 * Customize the drawing of {@link Sprite}s.
 *
 * @param scale            the scale of the {@link Sprite}
 * @param opacity          the opacity of the {@link Sprite}
 * @param rotation         the {@link Rotation} of the {@link Sprite}
 * @param isFlipHorizontal is the {@link Sprite} flipped horizontally
 * @param isFlipVertical   is the {@link Sprite} flipped vertically
 * @see Screen#drawSprite(Sprite, Offset, SpriteDrawOptions)
 */
public record SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, boolean isFlipHorizontal,
                                boolean isFlipVertical) implements Serializable {

    //TODO Replace flip with isFlipHorizontalisFlip and isFlipVertical booleans

    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), false, false);
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
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical);
    }

    /**
     * Creates a new instance with updated {@link #rotation()}.
     */
    public SpriteDrawOptions rotation(final Rotation rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical);
    }

    //TODO Doc
    public SpriteDrawOptions flipHorizontal(boolean flipHorizontal) {
        return new SpriteDrawOptions(scale, opacity, rotation, flipHorizontal, isFlipVertical);
    }

    //TODO Doc
    public SpriteDrawOptions flipVertical(boolean flipVertical) {
        return new SpriteDrawOptions(scale, opacity, rotation, flipVertical, isFlipVertical);
    }
}
